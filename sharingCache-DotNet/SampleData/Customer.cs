using Alachisoft.NCache.Runtime.Caching;
using Alachisoft.NCache.Runtime.JSON;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SampleData
{

    /// <summary>
    /// Model class for Customers
    /// </summary>
    [Serializable]
    [QueryIndexable]
    public class Customer
    {
        public Customer()
        { }

        /// <summary>
        /// Unique Id of the customer
        /// </summary>
        public string CustomerID
        {
            get;
            set;
        }

        /// <summary>
        /// Contact name of the customer
        /// </summary>
        public virtual string ContactName
        {
            set;
            get;
        }

        /// <summary>
        /// Company the customer works for
        /// </summary>
        public virtual string CompanyName
        {
            set;
            get;
        }

        /// <summary>
        /// Contact number of the customer
        /// </summary>
        public virtual string ContactNo
        {
            set;
            get;
        }

        /// <summary>
        /// Residential address of the customer
        /// </summary>
        public virtual string Address
        {
            set;
            get;
        }

        /// <summary>
        /// Residence city of the customer
        /// </summary>
        public virtual string City
        {
            set;
            get;
        }

        /// <summary>
        /// Nationality of the customer
        /// </summary>
        public virtual string Country
        {
            set;
            get;
        }

        /// <summary>
        /// Postal code of the customer
        /// </summary>
        public virtual string PostalCode
        {
            set;
            get;
        }

        /// <summary>
        /// Fax number of the customer
        /// </summary>
        public virtual string Fax
        {
            set;
            get;
        }
        public JsonObject toJson()
        {
            var jsonObject = new JsonObject();

            jsonObject.AddAttribute("CustomerID", (JsonValue)this.CustomerID);
            jsonObject.AddAttribute("ContactName", (JsonValue)this.ContactName);
            jsonObject.AddAttribute("CompanyName", (JsonValue)this.CompanyName);
            jsonObject.AddAttribute("ContactNo", (JsonValue)this.ContactNo);
            jsonObject.AddAttribute("Address", (JsonValue)this.Address);

            return jsonObject;
        }

        public static Customer fromJson(JsonObject jsonObject)
        {

            Customer customer = new Customer();

            customer.CustomerID = (jsonObject["CustomerID"] as JsonValue).ToStringValue();
            customer.ContactName = (jsonObject["ContactName"] as JsonValue).ToStringValue() ?? "";
            customer.CompanyName = (jsonObject["CompanyName"] as JsonValue).ToStringValue() ?? "";
            customer.ContactNo = (jsonObject["ContactNo"] as JsonValue).ToStringValue() ?? "";
            customer.Address = (jsonObject["Address"] as JsonValue).ToStringValue();

            return customer;
        }



    }
}
