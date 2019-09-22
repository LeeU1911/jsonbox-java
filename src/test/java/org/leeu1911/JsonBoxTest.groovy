package org.leeu1911

import spock.lang.Specification

def class JsonBoxTest extends Specification {
    def setup() {
        JsonBox.boxId = "json_box_java_test_22092019"
    }

    def "create record"() {
        given:
        String object = "{\"name\": \"json_box_test_record_1\"}"

        when:
        def result = JsonBoxObject.create(object)

        then:
        result != null
        result != ""
        result.contains("_id")
        result.contains("_createdOn")
    }
}