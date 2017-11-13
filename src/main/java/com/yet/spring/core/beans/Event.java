package com.yet.spring.core.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class Event {
    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    private int id;
    private String msg;

    @Value("#{new java.util.Date()}")
    private Date date;

    @Value("#{T(java.text.DateFormat).getDateTimeInstance()}")
    private DateFormat df;

    public Event() {
        id = AUTO_ID.getAndIncrement();
    }

    public Event(Date date, DateFormat df) {
        this();
        this.date = date;
        this.df = df;
    }

    public static boolean isDay(int start, int end) {
        LocalTime time = LocalTime.now();
        return time.getHour() > start && time.getHour() < end;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", msg=" + msg + ", date="
                + (df != null ? df.format(date) : date) + "]\n";
    }
}
