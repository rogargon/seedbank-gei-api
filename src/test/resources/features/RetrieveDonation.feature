Feature: Retrieve Donation
  In order to see donations
  As a user
  I want to retrieve donations

  Background:
    Given There is a registered donor with username "user" and password "password" and email "user@sample.app"
    And I can login with username "user" and password "password"
    And The response code is 200
    And User "user" is the donor
    And A valid take action exists
    And The donor creates a donation from the take action
    And The response code is 201
    And The donor creates a donation from the take action
    And The response code is 201

  Scenario: List all donations
    When I retrieve all donations
    Then The response code is 200
    And The response contains 2 donations