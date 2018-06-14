package com.honeycomb.hexagon.sample.modules.server;

import com.honeycomb.basement.provider.IProvider;
import com.honeycomb.basement.provider.SingletonProvider;
import com.honeycomb.hexagon.register.ModuleRegistration;

public class ServerModule extends ModuleRegistration {

    @Override
    protected void onRegister() {
        label("Server");
        
        controller(AppServer.class, provideAppServer());
        controller(ConsoleServer.class, provideConsoleServer());

        service(ServerActionDispatcherService.class, provideServerActionDispatcherService());
    }

    private static IProvider<AppServer> provideAppServer() {
        return new SingletonProvider<AppServer>() {
            @Override
            protected AppServer createInstance() {
                return new AppServer();
            }
        };
    }

    private static IProvider<ConsoleServer> provideConsoleServer() {
        return new SingletonProvider<ConsoleServer>() {
            @Override
            public ConsoleServer createInstance() {
                return new ConsoleServer();
            }
        };
    }

    private static IProvider<ServerActionDispatcherService> provideServerActionDispatcherService() {
        return new SingletonProvider<ServerActionDispatcherService>() {
            @Override
            public ServerActionDispatcherService createInstance() {
                return new ServerActionDispatcherService();
            }
        };
    }
}
