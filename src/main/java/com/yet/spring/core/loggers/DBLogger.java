package com.yet.spring.core.loggers;

import com.yet.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Component("dbLogger")
public class DBLogger extends AbstractLogger {
    private static final String SQL_ERROR_STATE_SCHEMA_EXISTS = "X0Y68";
    private static final String SQL_ERROR_STATE_TABLE_EXISTS = "X0Y32";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${jdbc.username}")
    private String schema;

    @PostConstruct
    public void init() {
        createDBSchema();
        createTableIfNotExists();
        updateEventAutoId();
    }

    @PreDestroy
    public void destroy() {
        int totalEvents = getTotalEvents();
        System.out.println("Total events in the DB: " + totalEvents);

        List<Event> allEvents = getAllEvents();
        String allEventIds = allEvents.stream()
                                      .map(Event::getId)
                                      .map(String::valueOf)
                                      .collect(Collectors.joining(", "));
        System.out.println("All DB Event ids: " + allEventIds);
    }

    private void createDBSchema() {
        try {
            jdbcTemplate.update("CREATE SCHEMA " + schema);
        } catch (DataAccessException e) {
            Throwable causeException = e.getCause();
            if (causeException instanceof SQLException) {
                SQLException sqlException = (SQLException) causeException;
                if (sqlException.getSQLState().equals(SQL_ERROR_STATE_SCHEMA_EXISTS)) {
                    System.out.println("Schema already exists");
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private void createTableIfNotExists() {
        try {
            jdbcTemplate.update("CREATE TABLE t_event (" + "id INT NOT NULL PRIMARY KEY," + "date TIMESTAMP,"
                    + "msg VARCHAR(255)" + ")");

            System.out.println("Created table t_event");
        } catch (DataAccessException e) {
            Throwable causeException = e.getCause();
            if (causeException instanceof SQLException) {
                SQLException sqlException = (SQLException) causeException;
                if (sqlException.getSQLState().equals(SQL_ERROR_STATE_TABLE_EXISTS)) {
                    System.out.println("Table already exists");
                } else {
                    throw e;
                }
            } else {
                throw e;
            }
        }
    }

    private void updateEventAutoId() {
        int maxId = getMaxId();
        Event.initAutoId(maxId + 1);
        System.out.println("Initialized Event.AUTO_ID to " + maxId);
    }

    private int getMaxId() {
        Integer count = jdbcTemplate.queryForObject("SELECT max(id) FROM t_event", Integer.class);
        return count != null ? count.intValue() : 0;
    }

    @Override
    public void logEvent(Event event) {
        jdbcTemplate.update("INSERT INTO t_event (id, date, msg) VALUES (?,?,?)", event.getId(), event.getDate(),
                event.getMsg());
        System.out.println("Saved to DB event with id " + event.getId());
    }

    public int getTotalEvents() {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM t_event", Integer.class);
        return count != null ? count.intValue() : 0;
    }

    public List<Event> getAllEvents() {
        final List<Event> query = jdbcTemplate.query("SELECT * FROM t_event",
                (rs, rowNum) -> new Event(
                        rs.getInt("id"),
                        rs.getDate("date"),
                        rs.getString("msg")));
        return query;
    }

    @Value("DataBase logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}