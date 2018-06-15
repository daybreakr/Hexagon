package com.honeycomb.hexagon.sample.modules.server;

import com.honeycomb.hexagon.register.ModuleRegistration;
import com.honeycomb.provider.IProvider;
import com.honeycomb.provider.SingletonProvider;

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
