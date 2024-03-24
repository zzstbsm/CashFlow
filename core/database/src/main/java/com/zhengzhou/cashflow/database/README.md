# Database

The database follows the Singleton pattern in order to ensure that only a single instance of the module is applied. The database is initialized in [`ApplicationCashFlow.kt`](../../../../../../../../../app/src/main/java/com/zhengzhou/cashflow/ApplicationCashFlow.kt).

## API

The central access point for interacting with the database of the application lies in the package [`API/use_case/`](api/use_case).
It implements all the functionality needed from outside the module and all the supported use cases of the application.

## Structure

The central access point is defined by the use cases implemented in  [`api/use_case/`](api/use_case), they are the classes that do the business work to format the data from the database to the user requirements.

Under the hood, the single instance of the database is created and saved in [`DatabaseInstance`](api/DatabaseInstance.kt): it is the private variable `DatabaseInstance.INSTANCE` that points to the Room database. Call `DatabaseInstance.get()` to retrieve the Database object pointer.

The implementations in [`API/repository/`](api/repository/) fetch real data from the database, these classes are modeled based on interfaces defined in [`data/repository/`](data/repository/).

With this structure

### Add new table in database

In order to add a new table in the database:
1. Add the data class in [`Database`](data/data_source/Database.kt), create a TypeConverter if necessary.
2. Update the version of the database.
3. Create a new Dao interface in the folder `databaseDaoComponents` with the convention `DataClassNameDao`.
4. Implement this new Dao in `DatabaseDao.
5. Create a new file for the SQLite queries in `databaseRepositoryComponents` with the convention `DataClassNameInterface`.
6. Implement this new interface in `DatabaseRepository`.
