package org.example;

import com.alachisoft.ncache.client.Cache;
import com.alachisoft.ncache.client.CacheManager;
import com.alachisoft.ncache.client.CacheItem;
import com.alachisoft.ncache.client.QueryCommand;
import com.alachisoft.ncache.runtime.JSON.JsonObject;
import com.alachisoft.ncache.runtime.JSON.JsonValue;
import com.alachisoft.ncache.runtime.caching.Tag;
import com.alachisoft.ncache.runtime.caching.expiration.Expiration;
import com.alachisoft.ncache.runtime.caching.expiration.ExpirationType;
import com.alachisoft.ncache.runtime.exceptions.CacheException;
import com.alachisoft.ncache.runtime.exceptions.OperationFailedException;
import com.alachisoft.ncache.runtime.util.TimeSpan;
import com.alachisoft.ncache.runtime.events.EventDataFilter;
import com.alachisoft.ncache.runtime.events.EventType;
import java.util.EnumSet;
import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 Class that provides the functionality of the sample
 */
public class BasicOperation
{
    private static Cache _cache;

    /**
     Generates instance of Customer to be used in this sample
     @return  returns instance of Customer
     */
    private static Customer createNewCustomer()
    {
        Customer tempVar = new Customer();
        tempVar.setCustomerID ("DAVJ0071");
        tempVar.setContactName ("David Johnes");
        tempVar.setCompanyName ("Lonesome Pine");
        tempVar.setContactNo ( "12345-6789");
        tempVar.setAddress ("Silicon Valley, Santa Clara, California");
        return tempVar;
    }

    /**
     Generates a string key for specified customer
     @param customer Instance of Customer to generate a key
     @return  returns a key
     */
    private static String getKey(Customer customer)
    {
        return String.format("Customer:%1$s", customer.getCustomerID());
    }

    public static void getOperationUsingQuery() throws CacheException, IOException, ClassNotFoundException {
        String query = "Select $Value$ FROM Alachisoft.NCache.Runtime.JSON.JsonObject WHERE $Tag$ = ?";
        var queryCommand = new QueryCommand(query);
        queryCommand.getParameters().put("$Tag$","East Coast Customers");
        var queryResult = _cache.getSearchService().executeReader(queryCommand);

        // QueryResult contains all the keys and metadata of result
        if (queryResult.getFieldCount() > 0)
        {
            while(queryResult.read())
            {
                JsonObject r = queryResult.getValue(1,JsonObject.class);

               String result =  queryResult.getValue(0,String.class);
               Customer customer =     fromJsonObjectToCustomer(r);

               System.out.println(result+" Result "+customer);
                // Perform operation according to your logic
            }
        }
        else
        {
            System.out.println("Empty");

            // No data containing the named tag(s) exist
        }
    }

    /**
     This method prints detials of customer type.
     @param jsonObject Json Object from cache
     */
    private static Customer fromJsonObjectToCustomer(JsonObject jsonObject)
    {

        JsonValue ID = (JsonValue)((jsonObject.getAttributeValue("CustomerID") instanceof JsonValue) ? jsonObject.getAttributeValue("CustomerID") : null);
        JsonValue ContactName = (JsonValue)((jsonObject.getAttributeValue("ContactName") instanceof JsonValue) ? jsonObject.getAttributeValue("ContactName") : null);
        JsonValue CompanyName = (JsonValue)((jsonObject.getAttributeValue("CompanyName") instanceof JsonValue) ? jsonObject.getAttributeValue("CompanyName") : null);
        JsonValue ContactNo = (JsonValue)((jsonObject.getAttributeValue("ContactNo") instanceof JsonValue) ? jsonObject.getAttributeValue("ContactNo") : null);
        JsonValue Address = (JsonValue)((jsonObject.getAttributeValue("Address") instanceof JsonValue) ? jsonObject.getAttributeValue("Address") : null);


        Customer customer = new Customer();
        customer.setCustomerID(ID.toStringValue());
        customer.setContactName(ContactName.toStringValue());
        customer.setCompanyName(CompanyName.toStringValue());
        customer.setAddress(Address.toStringValue());


        System.out.println(customer);
        System.out.println("Customer Details are as follows: ");
        System.out.println("Customer ID: " + ID);
        System.out.println("ContactName: " + ContactName);
        System.out.println("CompanyName: " + CompanyName);
        System.out.println("Contact No: " + ContactNo);
        System.out.println("Address: " + Address);
        System.out.println();

        return customer;
    }

    private static JsonObject populateJSONObjectFromCustomer(Customer customer) throws OperationFailedException {
        JsonObject jsonObject = new JsonObject();



        jsonObject.addAttribute( "CustomerID", new JsonValue(customer.getCustomerID()));
        jsonObject.addAttribute( "ContactName", new JsonValue (customer.getContactName()));
        jsonObject.addAttribute( "CompanyName", new JsonValue (customer.getCompanyName()));
        jsonObject.addAttribute( "ContactNo", new JsonValue (customer.getContactNo()));
        jsonObject.addAttribute( "Address", new JsonValue (customer.getAddress()));

        return  jsonObject;
    }
    /**
     This method initializes the cache
     */
    private static void initializeCache() throws Exception {
        //Properties properties=  getProperties();
        String cacheName="getKeysCache";//properties.getProperty("CacheID");

        if (cacheName == null || cacheName.isEmpty())
        {
            System.out.println("The CacheID cannot be null or empty.");
            return;
        }

        // Initialize an instance of the cache to begin performing operations:
        _cache = CacheManager.getCache(cacheName);

        // Print output on console
        System.out.println(String.format("\nCache '%1$s' is initialized.", cacheName));
    }


    /**
     This method adds json object in the cache using synchronous api
     @param key String key to be added in cache
     @param jsonObject Instance of JsonObject that will be added to cache
     */
    private static void addJsonObjectToCache(String key, JsonObject jsonObject) throws CacheException {

        //Populating cache item
        CacheItem item = new CacheItem(jsonObject);

        // Specify Tags
        Tag[] tags = new Tag[2];
        tags[0] = new Tag("East Coast Customers");
        tags[1] = new Tag("Important Customers");
        item.setTags (Arrays.asList(tags));


        // Adding cacheitem to cache
        _cache.add(key, item);

        // Print output on console
        System.out.println("\nJSON Object is added to cache.");
    }

    /**
     This method gets a json object from the cache using synchronous api
     @param key String key to get Json object from cache
     @return  returns instance of JsonObject retrieved from cache
     */
    private static JsonObject getJsonObjectFromCache(String key) throws CacheException {
        JsonObject cachedJsonObject = _cache.get(key,JsonObject.class);

        // Print output on console
        System.out.println("\nJSON Object is fetched from cache");

      // Customer customer = fromJsonObjectToCustomer(cachedJsonObject);

        return cachedJsonObject;
    }


    /**
     This method updates json object in the cache using synchronous api
     @param key String key to be updated in cache
     @param jsonObject Instance of JsonObject that will be updated in the cache
     */
    private static void updateJsonObjectInCache(String key, JsonObject jsonObject) throws CacheException {
        // Update item with a sliding expiration of 30 seconds
        jsonObject.setItem("CompanyName",  new JsonValue("Gourmet Lanchonetes"));

        TimeSpan expirationInterval = new TimeSpan(0, 0, 30);

        Expiration expiration = new Expiration(ExpirationType.Sliding);
        expiration.setExpireAfter( expirationInterval);

        CacheItem item = new CacheItem(jsonObject);
        item.setExpiration(expiration);

        _cache.insert(key, jsonObject);

        // Print output on console
        System.out.println("\nJSON Object is updated in cache.");
    }

    /**
     Remove a json object in the cache using synchronous api
     @param key String key to be deleted from cache
     */
    private static void deleteJsonObjectFromCache(String key) throws CacheException {
        // Remove the existing json object
        _cache.delete(key);

        // Print output on console
        System.out.println("\nJSON Object is deleted from cache.");
    }


    private static void RegisterNotification() throws CacheException {
        //Event notifications must be enabled in NCache manager->Options for events to work
        EnumSet<EventType> e = EnumSet.allOf(EventType.class);
        _cache.getMessagingService().addCacheNotificationListener(new CacheDataModificationImpl(), e, EventDataFilter.Metadata);
    }
    /**
     Executing this method will perform all the operations of the sample
     */
    public static void run() throws Exception {
        // Initialize cache
        initializeCache();
        //Regester Notification
        RegisterNotification();
        // Create a simple customer object
        Customer customer = createNewCustomer();

        //Method 1 for converting to Json
        JsonObject json_Object =  customer.toJson();

        //Method 2 for converting to Json
        JsonObject method2 = populateJSONObjectFromCustomer(customer);


        String key = getKey(customer);

      // addJsonObjectToCache(key,json_Object);

        JsonObject object =    getJsonObjectFromCache("Customer:DAVJ0078");

        //Method 1 for converting to Custom object
        Customer cus =  Customer.fromJson(object);

        //Method 2 for converting to Custom object Directly
        Customer CustomerMethod2 = fromJsonObjectToCustomer(object);

        System.out.println(cus);
         // Adding item synchronously
         // addJsonObjectToCache(key, jsonObject);

        // Get the object from cache
         // jsonObject = getJsonObjectFromCache(key);

         // getOperationUsingQuery();


       // Modify the object and update in cache
         updateJsonObjectInCache(key, method2);


        //  getJsonObjectFromCache(key);
        // Remove the existing object from cache
        //  deleteJsonObjectFromCache(key);

        // Dispose the cache once done
        // _cache.close();
    }
}