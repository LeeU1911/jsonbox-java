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

    def class TestEntity {
        String _id
        String _createdOn
        String name
    }
}