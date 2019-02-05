package edu.shapo.exprs.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import edu.shapo.exprs.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.utils.CollectionUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class AccountDaoImpl implements AccountDao {

    private static final Logger log = LogManager.getLogger(AccountDaoImpl.class);

    private Properties appProps;

    private Dao<Account, String> accountDao = null;

    private List<Account> bankAccounts = Collections.emptyList();

    public AccountDaoImpl() {
        loadProperties();
        initConnection();
        loadAccounts();
    }

    private void loadProperties() {
        appProps = new Properties();
        try {
            appProps.load(AccountDaoImpl.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            log.error("Could not read database properties", e);
        }
    }

    private void initConnection() {
        if (accountDao == null) {
            ConnectionSource connectionSource;
            try {
                connectionSource = new JdbcConnectionSource(appProps.getProperty("db.url"),
                        appProps.getProperty("db.username"),
                        appProps.getProperty("db.password"));
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

    public void closeConnection(){
        try {
            accountDao.getConnectionSource().close();
        } catch (IOException e) {
            log.error("Error closing connection", e);
        }
    }

}
