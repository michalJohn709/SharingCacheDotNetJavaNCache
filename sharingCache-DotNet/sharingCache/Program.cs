// See https://aka.ms/new-console-template for more information


namespace sharingCache
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {

                BasicOperation.Run();
                Console.ReadKey();

            }
            catch (Exception e)
            {
                Console.WriteLine("{e.ToString()}" + e.ToString());

            }

        }

    }
}