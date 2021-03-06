package io.github.jmgarridopaz.bluezone.adapters.forparkingcars.test.stepdefs;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.jmgarridopaz.bluezone.hexagon.driverports.forparkingcars.MoneyDto;
import io.github.jmgarridopaz.bluezone.hexagon.driverports.forparkingcars.PaymentCardData;
import io.github.jmgarridopaz.bluezone.hexagon.driverports.forparkingcars.PermitRequest;
import io.github.jmgarridopaz.bluezone.hexagon.driverports.forparkingcars.PermitTicket;


public class PermitStepDefs {

	private final ScenarioContext scenarioContext;
	
	public PermitStepDefs ( ScenarioContext scenarioContext ) {
		this.scenarioContext = scenarioContext;
	}


	@When("I do the following permit issuing request:")
	public void iDoTheFollowingPermitIssuingRequest ( PermitRequest permitRequest ) {
		PermitTicket permitTicket = this.scenarioContext.forParkingCars().issuePermit ( this.scenarioContext.clock(), permitRequest );
		this.scenarioContext.setPermitTicket(permitTicket);
	}

	@Then("I should get the following permit ticket:")
	public void iShouldGetTheFollowingPermitTicket ( PermitTicket expectedPermitTicket ) {
		PermitTicket returnedPermitTicket = this.scenarioContext.permitTicket();
		String assertionErrorMsg =  "Expected permit ticket ("+expectedPermitTicket+") is not equal to returned permit ticket ("+returnedPermitTicket+")";
		assertEquals ( assertionErrorMsg , expectedPermitTicket, returnedPermitTicket );
	}

	
	@DataTableType
    public PermitRequest permitRequestEntry ( Map<String, String> dataTableEntry ) {
		
		LocalDateTime endingDateTime = LocalDateTime.parse ( dataTableEntry.get("endingDateTime"), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm") );

		YearMonth paymentCardExpirationDate = YearMonth.parse ( dataTableEntry.get("paymentCardExpirationDate"), DateTimeFormatter.ofPattern("yyyy/MM"));
		PaymentCardData paymentCard = new PaymentCardData();
		paymentCard.setNumber(dataTableEntry.get("paymentCardNumber"));
		paymentCard.setCvv(dataTableEntry.get("paymentCardCvv"));
		paymentCard.setExpirationDate(paymentCardExpirationDate);

		PermitRequest permitRequest = new PermitRequest();
		permitRequest.setCarPlate(dataTableEntry.get("carPlate"));
		permitRequest.setRateName(dataTableEntry.get("rateName"));
		permitRequest.setEndingDateTime(endingDateTime);
		permitRequest.setPaymentCard(paymentCard);
		
		return permitRequest;
    }

	
	@DataTableType
    public PermitTicket permitTicketEntry ( Map<String, String> dataTableEntry ) {

		LocalDateTime startingDateTime = LocalDateTime.parse ( dataTableEntry.get("startingDateTime"), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm") );
		LocalDateTime endingDateTime = LocalDateTime.parse ( dataTableEntry.get("endingDateTime"), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm") );

		MoneyDto price = new MoneyDto();
		price.setAmount ( new BigDecimal(dataTableEntry.get("priceAmount")) );
		price.setCurrencySymbol ( dataTableEntry.get("priceCurrencySymbol") );
		
		PermitTicket permitTicket = new PermitTicket();
		permitTicket.setCode(dataTableEntry.get("code"));
		permitTicket.setCarPlate(dataTableEntry.get("carPlate"));
		permitTicket.setStartingDateTime(startingDateTime);
		permitTicket.setEndingDateTime(endingDateTime);
		permitTicket.setRateName(dataTableEntry.get("rateName"));
		permitTicket.setPrice(price);
		
		return permitTicket;
	}
	
}
