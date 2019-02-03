package edu.shapo.exprs.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import edu.shapo.exprs.model.Account;
import spark.utils.CollectionUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AccountDaoImpl implements AccountDao {

    public static final String URL_WITH_INIT_SCRIPTS = "jdbc:h2:mem:bank;INIT=runscript from 'classpath:schema.sql'\\;runscript from 'classpath:accounts_load.sql'";

    private Dao<Account, String> accountDao = null;

    private List<Account> bankAccounts = Collections.EMPTY_LIST;

    public AccountDaoImpl() {
        initConnection();
    }

    private void initConnection() {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(URL_WITH_INIT_SCRIPTS, "sa", "");
            accountDao = DaoManager.createDao(connectionSource, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Account> getAllAccounts() {

        if (CollectionUtils.isEmpty(bankAccounts)) {
            try {
                bankAccounts = accountDao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return bankAccounts;
    }

}
