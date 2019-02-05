package edu.shapo.exprs.api;

import com.google.gson.Gson;
import com.google.inject.Inject;
import edu.shapo.exprs.model.Account;
import edu.shapo.exprs.service.AccountService;
import edu.shapo.exprs.to.AccountTO;
import org.h2.util.StringUtils;

public class AccountController {

    @Inject
    private AccountService accountService;

    public String findById(String idStr) {

        if (StringUtils.isNumber(idStr)) {
            long id = Long.parseLong(idStr);
            Account res = accountService.findById(id);
            if (res != null) {
                return new Gson().toJson(new AccountTO(res.getId(), res.getCurrentAmout()));
            }
        }

        return "Account not found by " + idStr;
    }




    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
