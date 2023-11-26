# Database

The database follows the Singleton pattern in order to ensure that only a single instance of the module is applied. The database is initialized in [`ApplicationCashFlow.kt`](../../../../../../../../../app/src/main/java/com/zhengzhou/cashflow/ApplicationCashFlow.kt).

## API

The central access point for interacting with the database of the application lies in the class [`DatabaseRepository`](DatabaseRepository.kt). It implements multiple interfaces to provide functionality related to the operations with the tables in the database defined in [`Database.kt`](Database.kt).

The interfaces that implement the functionality of the database are found in the folder `databaseRepositoryComponents`.
In order to add a new method, edit the interface with the desired feature.

## Structure

The central access point is `DatabaseRepository` that behaves as a public interface.

Under the hood, the single instance of the database is created and saved in [`DatabaseInstance`](DatabaseInstance.kt): it is the private variable `DatabaseInstance.INSTANCE` that points to the Room database. Call `DatabaseInstance.get()` to retrieve the Database object pointer.

The public methods implemented in `databaseRepositoryComponents` are able to call the database through the implemented in [`DatabaseDao`](DatabaseDao.kt). For maintainability of the code, the queries are stored in the folder `databaseDaoComponents` as interfaces separated per feature; in the end all the interfaces are implemented in the interface `DatabaseDao`.

### Add new table in database

In order to add a new table in the database:
1. Add the data class in [`Database`](Database.kt), create a TypeConverter if necessary.
2. Update the version of the database.
3. Create a new Dao interface in the folder `databaseDaoComponents` with the convention `DataClassNameDao`.
4. Implement this new Dao in `DatabaseDao.
5. Create a new file for the SQLite queries in `databaseRepositoryComponents` with the convention `DataClassNameInterface`.
6. Implement this new interface in `DatabaseRepository`.
