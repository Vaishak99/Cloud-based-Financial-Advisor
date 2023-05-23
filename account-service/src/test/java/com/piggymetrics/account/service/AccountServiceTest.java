package com.piggymetrics.account.service;

import com.piggymetrics.account.client.AuthServiceClient;
import com.piggymetrics.account.client.StatisticsServiceClient;
import com.piggymetrics.account.domain.*;
import com.piggymetrics.account.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountServiceTest {

	@InjectMocks
	private AccountServiceImpl accountService;

	@Mock
	private StatisticsServiceClient statisticsClient;

	@Mock
	private AuthServiceClient authClient;

	@Mock
	private AccountRepository repository;

	@Before
	public void setup() {
		initMocks(this);
	}

	@Test
	public void shouldFindByName() {

		final Account account = new Account();
		account.setName("test");

		when(accountService.findByName(account.getName())).thenReturn(account);
		Account found = accountService.findByName(account.getName());

		assertEquals(account, found);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenNameIsEmpty() {
		accountService.findByName("");
	}

	@Test
	public void shouldCreateAccountWithGivenUser() {

		User user = new User();
		user.setUsername("test");

		Account account = accountService.create(user);

		assertEquals(user.getUsername(), account.getName());
		assertEquals(0, account.getSaving().getAmount().intValue());
		assertEquals(Currency.getDefault(), account.getSaving().getCurrency());
		assertEquals(0, account.getSaving().getInterest().intValue());
		assertEquals(false, account.getSaving().getDeposit());
		assertEquals(false, account.getSaving().getCapitalization());
		assertNotNull(account.getLastSeen());

		verify(authClient, times(1)).createUser(user);
		verify(repository, times(1)).save(account);
	}

	@Test
	public void shouldSaveChangesWhenUpdatedAccountGiven() {

		Item grocery = new Item();
		grocery.setTitle("Grocery");
		grocery.setAmount(new BigDecimal(10));
		grocery.setCurrency(Currency.USD);
		grocery.setPeriod(TimePeriod.DAY);
		grocery.setIcon("meal");

		Item salary = new Item();
		salary.setTitle("Salary");
		salary.setAmount(new BigDecimal(9100));
		salary.setCurrency(Currency.USD);
		salary.setPeriod(TimePeriod.MONTH);
		salary.setIcon("wallet");

		Saving saving = new Saving();
		saving.setAmount(new BigDecimal(1500));
		saving.setCurrency(Currency.USD);
		saving.setInterest(new BigDecimal("3.32"));
		saving.setDeposit(true);
		saving.setCapitalization(false);

		final Account update = new Account();
		update.setName("test");
		update.setNote("test note");
		update.setIncomes(Arrays.asList(salary));
		update.setExpenses(Arrays.asList(grocery));
		update.setSaving(saving);

		final Account account = new Account();

		when(accountService.findByName("test")).thenReturn(account);
		accountService.saveChanges("test", update);

		assertEquals(update.getNote(), account.getNote());
		assertNotNull(account.getLastSeen());

		assertEquals(update.getSaving().getAmount(), account.getSaving().getAmount());
		assertEquals(update.getSaving().getCurrency(), account.getSaving().getCurrency());
		assertEquals(update.getSaving().getInterest(), account.getSaving().getInterest());
		assertEquals(update.getSaving().getDeposit(), account.getSaving().getDeposit());
		assertEquals(update.getSaving().getCapitalization(), account.getSaving().getCapitalization());

		
		
		verify(repository, times(1)).save(account);
		verify(statisticsClient, times(1)).updateStatistics("test", account);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldFailWhenNoAccountsExistedWithGivenName() {
		final Account update = new Account();
		update.setIncomes(Arrays.asList(new Item()));
		update.setExpenses(Arrays.asList(new Item()));

		when(accountService.findByName("test")).thenReturn(null);
		accountService.saveChanges("test", update);
	}
}
