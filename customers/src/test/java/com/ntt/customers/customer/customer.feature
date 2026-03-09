Feature: Integration Test For Customer Entity

  Background:
    * def portUrl = karate.properties['baseUrl']
    * url portUrl

  Scenario: Save Entity Successfully and check numberId
    Given path 'api/v1/customers'
    And request
      """
      {
        "name": "Victor",
        "gender": "Masculino",
        "age": 20,
        "idNumber": 1725082786,
        "address": "Calle Segovia y Raices",
        "phone": "0984565509",
        "password": "victorContrasena",
        "status": true
      }
      """
    When method post
    Then status 201
    And match response.message == "Cliente guardado exitosamente"

    Given path 'api/v1/customers'
    When method get
    Then status 200
    And match response[0].idNumber == 1725082786
    And match response[0].name == "Victor"