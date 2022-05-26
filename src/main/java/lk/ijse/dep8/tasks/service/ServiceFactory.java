package lk.ijse.dep8.tasks.service;

import lk.ijse.dep8.tasks.service.custom.impl.TaskServiceImpl;
import lk.ijse.dep8.tasks.service.custom.impl.UserServiceImpl;

import java.sql.Connection;

public class ServiceFactory {

    private static ServiceFactory serviceFactory;

    private ServiceFactory(){}

    public static ServiceFactory getInstance(){
        return (serviceFactory == null) ? serviceFactory = new ServiceFactory():serviceFactory;
    }

    public <T extends SuperService> T getService(Connection connection, ServiceType serviceType){
        switch (serviceType){
            case TASK: return (T) new TaskServiceImpl();
            case USER:  return (T) new UserServiceImpl();
            default: return null;
        }
    }
    public enum ServiceType{
        USER, TASK;
    }
}
