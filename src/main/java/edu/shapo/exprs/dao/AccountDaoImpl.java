package edu.shapo.exprs.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import edu.shapo.exprs.model.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AccountDaoImpl implements AccountDao {

    private static final Logger log = LogManager.getLogger(AccountDaoImpl.class);

    private Properties appProps;

    private Dao<Account, String> dao = null;

    private Map<Long, Account> bankAccounts = new ConcurrentHashMap<>();

    public AccountDaoImpl() {
        loadProperties();
        initConnection();
        loadAccounts();
    }

    @Override
    public List<Account> getAllAccounts() {
        if (bankAccounts.isEmpty()) {
            loadAccounts();
        }
        return new ArrayList<>(bankAccounts.values());
    }

    @Override
    public Optional<Account> findById(Long id) {
        if (bankAccounts.isEmpty()) {
            loadAccounts();
        }
        return Optional.ofNullable(bankAccounts.get(id));
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
        if (dao == null) {
            ConnectionSource connectionSource;
            try {
                connectionSource = new JdbcConnectionSource(appProps.getProperty("db.url"),
                        appProps.getProperty("db.username"),
                        appProps.getProperty("db.password"));
                dao = DaoManager.createDao(connectionSource, Account.class);
            } catch (SQLException e) {
                log.error("Error during init connection to DB", e);
            }
        }

    }

    private void loadAccounts(){
        if (bankAccounts.isEmpty()) {
            try {
                bankAccounts.putAll(
                        dao.queryForAll().stream().collect(Collectors.toMap(Account::getId, Function.identity()))
                );
            } catch (SQLException e) {
                log.error("Error loading accounts", e);
            }
        }
    }

    public void closeConnection(){
        try {
            dao.getConnectionSource().close();
        } catch (IOException e) {
            log.error("Error closing connection", e);
        }
    }

}
