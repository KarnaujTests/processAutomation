Feature: Example

  Scenario: Button modify search is shown in the page
    Given I make a booking from 'DUB' to 'Berlin' on 11/03/2019 for 2 adults and 1 child
    When I pay for booking with card details '5555 5555 5555 5557', expiration date '10/18' and security code '265'
    Then I should get payment declined message
    
    