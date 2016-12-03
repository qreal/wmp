# General rules of first line and infrastructure services separation 

Current implementation divides the system into two parts. The first one is for database and other infrastructure services and the second one is for the user-session services. Obviously, from the user-session you cannot (and should not) access the whole data. This is why responsibility for data is divided in two parts. First line services (Dashboard, Editor and other services interacting with users) cannot access data outside of user session. As for infrastructure services, they have access to the whole data. While implementing new features and services follow these instructions:

1. If a feature influences the objects that are available to the current user and no others, it belongs to first line services. Implement it within an already existing first line service or create a new one.

1. If a feature influences the objects which belong to various users, it should be placed somewhere in the infrastructure layer.

You can find ER-diagram of the data scheme in the [architecture project](https://github.com/qreal/wmp/tree/master/UML) (db-services-implementation-scheme diagram). An ER-diagram of the logical data scheme for a single user session is presented there as well (db-services-user-session-scheme diagram).

