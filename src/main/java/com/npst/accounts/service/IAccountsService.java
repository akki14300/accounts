package com.npst.accounts.service;

import com.npst.accounts.dao.CustomerDto;

public interface IAccountsService {

    void createAccounts(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateCustomerWithAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);


}
