package org.leeu1911

import spock.lang.Specification

def class JsonBoxTest extends Specification {
    def setup() {
        JsonBox.boxId = "json_box_java_test_22092019"
    }

    def "create record"() {
        given:
        TestEntity object = new TestEntity(name: "json_box_test_record_1")
        when:
        def result = JsonBoxObject.create(object, TestEntity.class)

        then:
        result != null
        result._id != null
        result._createdOn != null
        result.name == object.name
    }

    def "create record in collection"() {
        given:
        TestEntity object = new TestEntity(name: "json_box_test_record_1")
        when:
        def result = JsonBoxObject.create("test_collection", object, TestEntity.class)

        then:
        result != null
        result._id != null
        result._createdOn != null
        result.name == object.name
    }

    def "get record by id"() {
        given:
        TestEntity object = new TestEntity(name: "json_box_test_record_2")
        def created = JsonBoxObject.create(object, TestEntity.class)

        when:
        def result = JsonBoxObject.findById(created._id, TestEntity.class)

        then:
        result != null
        result._id != null
        result._createdOn != null
        result.name == created.name
    }

    def "update record"() {
        given:
        TestEntity object = new TestEntity(name: "json_box_test_record_3")
        def created = JsonBoxObject.create(object, TestEntity.class)

        when:
        created.name = "json_box_test_record_3_updated"
        def success = JsonBoxObject.update(created._id, created)

        then:
        success

        when:
        def updated = JsonBoxObject.findById(created._id, TestEntity.class)

        then:
        updated != null
        updated._id != null
        updated._createdOn != null
        updated.name == created.name
    }

    def "delete record"() {
        given:
        TestEntity object = new TestEntity(name: "json_box_test_record_4")
        def created = JsonBoxObject.create(object, TestEntity.class)

        when:
        def success = JsonBoxObject.deleteById(created._id)

        then:
        success

        when:
        def updated = JsonBoxObject.findById(created._id, TestEntity.class)

        then:
        updated == null
    }

    def "get records sorted"() {
        given:
        TestEntity firstObject = new TestEntity(name: "json_box_test_record_5")
        TestEntity secondObject = new TestEntity(name: "json_box_test_record_6")
        TestEntity thirdObject = new TestEntity(name: "json_box_test_record_7")
        def firstCreated = JsonBoxObject.create("sorted_collection", firstObject, TestEntity.class)
        def secondCreated = JsonBoxObject.create("sorted_collection", secondObject, TestEntity.class)
        def thirdCreated = JsonBoxObject.create("sorted_collection", thirdObject, TestEntity.class)

        when:
        List<TestEntity> results = JsonBoxObject.findAll("sorted_collection", "name", "DESC")

        then:
        results != null
        results.get(0) != null
        results.get(0).name == thirdCreated.name
        results.get(1) != null
        results.get(1).name == secondCreated.name
        results.get(2) != null
        results.get(2).name == firstCreated.name

        cleanup:
        JsonBoxObject.deleteById(firstCreated._id)
        JsonBoxObject.deleteById(secondCreated._id)
        JsonBoxObject.deleteById(thirdCreated._id)
    }

    def "get records with pagination"() {
        given:
        TestEntity firstObject = new TestEntity(name: "json_box_test_record_8")
        TestEntity secondObject = new TestEntity(name: "json_box_test_record_9")
        TestEntity thirdObject = new TestEntity(name: "json_box_test_record_10")
        def firstCreated = JsonBoxObject.create("page_collection", firstObject, TestEntity.class)
        def secondCreated = JsonBoxObject.create("page_collection", secondObject, TestEntity.class)
        def thirdCreated = JsonBoxObject.create("page_collection", thirdObject, TestEntity.class)

        when:
        List<TestEntity> results = JsonBoxObject.findAll("page_collection", 0, 2)

        then:
        results != null
        results.size() == 2
        results.get(0) != null
        results.get(0).name == thirdCreated.name
        results.get(1) != null
        results.get(1).name == secondCreated.name

        cleanup:
        JsonBoxObject.deleteById(firstCreated._id)
        JsonBoxObject.deleteById(secondCreated._id)
        JsonBoxObject.deleteById(thirdCreated._id)
    }

    def "get records sorted with pagination"() {
        given:
        TestEntity firstObject = new TestEntity(name: "json_box_test_record_11")
        TestEntity secondObject = new TestEntity(name: "json_box_test_record_12")
        TestEntity thirdObject = new TestEntity(name: "json_box_test_record_13")
        def firstCreated = JsonBoxObject.create("page_sort_collection", firstObject, TestEntity.class)
        def secondCreated = JsonBoxObject.create("page_sort_collection", secondObject, TestEntity.class)
        def thirdCreated = JsonBoxObject.create("page_sort_collection", thirdObject, TestEntity.class)

        when:
        List<TestEntity> results = JsonBoxObject.findAll("page_sort_collection", "name", "ASC", 0, 2)

        then:
        results != null
        results.size() == 2
        results.get(0) != null
        results.get(0).name == firstCreated.name
        results.get(1) != null
        results.get(1).name == secondCreated.name

        cleanup:
        JsonBoxObject.deleteById(firstCreated._id)
        JsonBoxObject.deleteById(secondCreated._id)
        JsonBoxObject.deleteById(thirdCreated._id)
    }

    def class TestEntity {
        String _id
        String _createdOn
        String name
    }
}