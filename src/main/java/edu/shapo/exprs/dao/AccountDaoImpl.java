package edu.shapo.exprs.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import edu.shapo.exprs.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.utils.CollectionUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountDaoImpl implements AccountDao {

    private static final String URL_WITH_INIT_SCRIPTS = "jdbc:h2:mem:bank;INIT=runscript from 'classpath:schema.sql'\\;runscript from 'classpath:accounts_load.sql'";

    private static final Logger log = LogManager.getLogger(AccountDaoImpl.class);

    private Dao<Account, String> accountDao = null;

    private List<Account> bankAccounts = Collections.emptyList();

    public AccountDaoImpl() {
        initConnection();
        loadAccounts();
    }

    private void initConnection() {
        if (accountDao == null) {
            ConnectionSource connectionSource;
            try {
                connectionSource = new JdbcConnectionSource(URL_WITH_INIT_SCRIPTS, "sa", "");
                accountDao = DaoManager.createDao(connectionSource, Account.class);
            } catch (SQLException e) {
                log.error("Error during init connection to DB", e);
            }
        }

    }

    private void loadAccounts(){
        if (CollectionUtils.isEmpty(bankAccounts)) {
            try {
                bankAccounts = new CopyOnWriteArrayList<>(accountDao.queryForAll());
            } catch (SQLException e) {
                log.error("Error loading accounts", e);
            }
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        if (CollectionUtils.isEmpty(bankAccounts)) {
            loadAccounts();
        }
        return bankAccounts;
    }

    @Override
    public Account findById(Long id) {
        if (CollectionUtils.isEmpty(bankAccounts)) {
            loadAccounts();
        }
        Optional<Account> optionalAccount = bankAccounts.stream().filter(a -> a.getId().equals(id)).findFirst();
        return optionalAccount.orElse(null);
    }
}
