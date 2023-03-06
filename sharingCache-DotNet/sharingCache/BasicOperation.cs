using Alachisoft.NCache.Client;
using Alachisoft.NCache.Runtime.Caching;
using SampleData;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;
using Alachisoft.NCache.Runtime.JSON;
using Newtonsoft.Json.Linq;
using System.Collections;

namespace sharingCache
{
    public class BasicOperation
    {
        private static ICache _cache;


        /// <summary>
        /// This method initializes the cache
        /// </summary>
        private static void InitializeCache()
        {
            string cache = "getKeysCache";//ConfigurationManager.AppSettings["CacheID"];

            if (String.IsNullOrEmpty(cache))
            {
                Console.WriteLine("The CacheID cannot be null or empty.");
                return;
            }

            // Initialize an instance of the cache to begin performing operations:
            _cache = CacheManager.GetCache(cache);

            // Print output on console
            Console.WriteLine(string.Format("\nCache '{0}' is initialized.", cache));
        }

        /// <summary>
        /// Generates instance of Customer to be used in this sample
        /// </summary>
        /// <returns> returns instance of Customer </returns>
        private static Customer CreateNewCustomer()
        {
            return new Customer
            {
                CustomerID = "DAVOO",
                ContactName = "David Johnes",
                CompanyName = "Lonesome Pine Restaurant",
                ContactNo = "12345-6789",
                Address = "Silicon Valley, Santa Clara, California",
            };
        }

        private static JsonObject PopulateJSONObjectFromCustomer(Customer customer)
        {
            // Method 1
            //  string jsonString = Newtonsoft.Json.JsonConvert.SerializeObject(customer);

            //    return new JsonObject(jsonString);

            // Method 2

            var jsonObject = new JsonObject();

            jsonObject.AddAttribute("CustomerID", (JsonValue)customer.CustomerID);
            jsonObject.AddAttribute("ContactName", (JsonValue)customer.ContactName);
            jsonObject.AddAttribute("CompanyName", (JsonValue)customer.CompanyName);
            jsonObject.AddAttribute("ContactNo", (JsonValue)customer.ContactNo);
            jsonObject.AddAttribute("Address", (JsonValue)customer.Address);

            return jsonObject;
        }

        /// <summary>
        /// Generates a string key for specified customer
        /// </summary>
        /// <param name="customer"> Instance of Customer to generate a key</param>
        /// <returns> returns a key </returns>
        private static string GetKey(Customer customer)
        {
            return string.Format("Customer:{0}", customer.CustomerID);
        }

        /// <summary>
        /// This method adds json object in the cache using synchronous api
        /// </summary>
        /// <param name="key"> String key to be added in cache </param>
        /// <param name="jsonObject"> Instance of JsonObject that will be added to cache </param>
        private static void AddJsonObjectToCache(string key, JsonObject jsonObject)
        {
            //Populating cache item
            CacheItem item = new CacheItem(jsonObject);

            //Tag with cache Item
            item.Tags = new Tag[] { new Tag("East Coast Customers") };

            // Adding cacheitem to cache 
            _cache.Add(key, item);

            // Print output on console
            Console.WriteLine("\nJSON Object is added to cache.");
        }


        public static ICollection<string> getUsingTagAPI(Tag tag)
        {
          ICollection<string> result =   _cache.SearchService.GetKeysByTag(tag);
          return result;
        }
        
        public static void getOperationUsingQuery()
        {
            string query = "Select * FROM Alachisoft.NCache.Runtime.JSON.JsonObject WHERE $Tag$ = ?";
            var queryCommand = new QueryCommand(query);
            queryCommand.Parameters.Add("$Tag$", "Temporary");
            
            var queryResult = _cache.SearchService.ExecuteReader(queryCommand);

            // QueryResult contains all the keys and metadata of result
            if (queryResult.FieldCount > 0)
            {
                while (queryResult.Read())
                {
                  string key =   queryResult.GetValue<string>(0);
                    
                   // Perform operation according to your logic
                }
            }
            else
            {
                // No data containing the named tag(s) exist
            }
        }


        /// <summary>
        /// This method gets a json object from the cache using synchronous api
        /// </summary>
        /// <param name="key"> String key to get Json object from cache</param>
        /// <returns> returns instance of JsonObject retrieved from cache</returns>
        private static JsonObject GetJsonObjectFromCache(string key)
        {
            JObject cacheJsonObject = _cache.Get<JObject>(key); //Newtonsoft.Json.JsonConvert.DeserializeObject (_cache.Get<string>(key));

            JsonObject temp = (JsonObject)JsonObject.Parse(cacheJsonObject.ToString());

            // Print output on console
            Console.WriteLine("\nJSON Object is fetched from cache");

            return temp;
        }



        /// <summary>
        /// This method updates json object in the cache using synchronous api
        /// </summary>
        /// <param name="key"> String key to be updated in cache</param>
        /// <param name="jsonObject"> Instance of JsonObject that will be updated in the cache</param>
        private static void UpdateJsonObjectInCache(string key, JsonObject jsonObject)
        {
            // Update item with a sliding expiration of 30 seconds
            jsonObject["CompanyName"] = (JsonValue)"Gourmet Lanchonetes";

            TimeSpan expirationInterval = new TimeSpan(1, 0, 30);

            Expiration expiration = new Expiration(ExpirationType.Sliding);
            expiration.ExpireAfter = expirationInterval;

            CacheItem item = new CacheItem(jsonObject);
            item.Expiration = expiration;

            _cache.Insert(key, jsonObject);

            // Print output on console
            Console.WriteLine("\nJSON Object is updated in cache.");
        }



        /// <summary>
        /// Remove a json object in the cache using synchronous api
        /// </summary>
        /// <param name="key"> String key to be deleted from cache</param>
        private static void RemoveJsonObjectFromCache(string key)
        {
            // Remove the existing json object
            _cache.Remove(key);

            // Print output on console
            Console.WriteLine("\nJSON Object is removed from cache.");
        }



        /// <summary>
        /// This method prints detials of customer type.
        /// </summary>
        /// <param name="jsonObject"></param>
        private static Customer fromJsonObjectToCustomer(JsonObject jsonObject)
        {

            Customer customer = new Customer();

            customer.CustomerID = (jsonObject["CustomerID"] as JsonValue).ToStringValue();
            customer.ContactName = (jsonObject["ContactName"] as JsonValue).ToStringValue() ?? "";
            customer.CompanyName = (jsonObject["CompanyName"] as JsonValue).ToStringValue() ?? "";
            customer.ContactNo = (jsonObject["ContactNo"] as JsonValue).ToStringValue() ?? "";
            customer.Address = (jsonObject["Address"] as JsonValue).ToStringValue();


            Console.WriteLine();
            Console.WriteLine("Customer Details are as follows: ");
            Console.WriteLine("Customer ID: " + customer.CustomerID);
            Console.WriteLine("ContactName: " + customer.ContactName);
            Console.WriteLine("CompanyName: " + customer.CompanyName);
            Console.WriteLine("Contact No: " + customer.ContactNo);
            Console.WriteLine("Address: " + customer.Address);
            Console.WriteLine();
            return customer;
        }

        /// <summary>
        /// Executing this method will perform all the operations of the sample
        /// </summary>
        public static void Run()
        {
            // Initialize cache
            InitializeCache();

            // Create a simple customer object
            Customer customer = CreateNewCustomer();

            //PoulateJSONObject
            JsonObject jsonObject = PopulateJSONObjectFromCustomer(customer);

            string key = GetKey(customer);

            // Adding item synchronously
            AddJsonObjectToCache(key, jsonObject);

            //Get JsonObject from Cache
           // JsonObject getJsonObject = GetJsonObjectFromCache(key);

            //Fetched Customer.
            //Customer fetchedCustomer = fromJsonObjectToCustomer(getJsonObject);

            // Dispose the cache once done
            //  _cache.Dispose();
        }
    }
}
