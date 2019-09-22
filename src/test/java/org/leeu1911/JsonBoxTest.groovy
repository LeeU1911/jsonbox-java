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

    def "get record by id"(){
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

    def "update record"(){
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

    def "delete record"(){
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

    def class TestEntity {
        String _id
        String _createdOn
        String name
    }
}