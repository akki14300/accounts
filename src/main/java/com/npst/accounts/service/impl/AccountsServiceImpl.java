package com.npst.accounts.service.impl;

import com.npst.accounts.constants.ApplicationConstants;
import com.npst.accounts.dao.AccountsDto;
import com.npst.accounts.dao.CustomerDto;
import com.npst.accounts.entity.Accounts;
import com.npst.accounts.entity.Customer;
import com.npst.accounts.exception.CustomerAlreadyExists;
import com.npst.accounts.exception.ResourceNotFoundException;
import com.npst.accounts.mapper.AccountsMapper;
import com.npst.accounts.mapper.CustomerMapper;
import com.npst.accounts.repository.AccountsRepository;
import com.npst.accounts.repository.CustomerRepository;
import com.npst.accounts.service.IAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class AccountsServiceImpl implements IAccountsService {

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public void createAccounts(CustomerDto customerDto) {
        log.info("Creating account for mobile number: {}", customerDto.getMobileNumber());

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalMobileNO = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        if (optionalMobileNO.isPresent()) {
            log.warn("Customer already exists with mobile number: {}", customerDto.getMobileNumber());
            throw new CustomerAlreadyExists("Customer already exists with given mobile no " + customerDto.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer saved with ID: {}", savedCustomer.getCustomerId());

        Accounts newAccount = createNewAccount(savedCustomer);
        accountsRepository.save(newAccount);
        log.info("New account created with account number: {} for customer ID: {}", newAccount.getAccountNumber(), savedCustomer.getCustomerId());
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(ApplicationConstants.SAVINGS);
        newAccount.setBranchAddress(ApplicationConstants.ADDRESS);
        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        log.info("Fetching customer and account for mobile number: {}", mobileNumber);

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> {
            log.error("Customer not found for mobile number: {}", mobileNumber);
            return new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber);
        });

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(() -> {
            log.error("Account not found for customer ID: {}", customer.getCustomerId());
            return new ResourceNotFoundException("Accounts", "customerId", customer.getCustomerId().toString());
        });

        log.info("Fetched customer and account data for customer ID: {}", customer.getCustomerId());
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountDto(accounts, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateCustomerWithAccount(CustomerDto customerDto) {
        log.info("Updating customer and account for mobile number: {}", customerDto.getMobileNumber());
        boolean isUpdated = false;

        AccountsDto accountsDto = customerDto.getAccountsDto();

        if (accountsDto != null) {
            log.debug("Account DTO is present. Proceeding with update.");
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(() -> {
                log.error("Account not found with account number: {}", accountsDto.getAccountNumber());
                return new ResourceNotFoundException("Account", "Account-Number", accountsDto.getAccountNumber().toString());
            });

            AccountsMapper.mapToAccount(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);
            log.info("Account updated with account number: {}", accounts.getAccountNumber());

            Integer customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
                log.error("Customer not found with ID: {}", customerId);
                return new ResourceNotFoundException("Customer", "Customer-Id", customerId.toString());
            });

            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            log.info("Customer updated with ID: {}", customerId);

            isUpdated = true;
        } else {
            log.warn("No account information provided for update.");
        }

        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        log.info("Deleting account and customer for mobile number: {}", mobileNumber);

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> {
            log.error("Customer not found for mobile number: {}", mobileNumber);
            return new ResourceNotFoundException("customer", "Mobile Number", mobileNumber);
        });

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        log.info("Successfully deleted customer and account for customer ID: {}", customer.getCustomerId());
        return true;
    }

}
