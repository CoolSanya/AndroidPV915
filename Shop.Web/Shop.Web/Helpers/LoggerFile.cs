namespace Shop.Web.Helpers
{
    public static class ApplicationLoggerFile
    {
        public static void LoggerFile(this WebApplication webApp)
        {
            using (var scope = webApp.Services.CreateScope())
            {
                var path = Path.Combine(Directory.GetCurrentDirectory(), "Logs");
                if (!Directory.Exists(path))
                {
                    Directory.CreateDirectory(path);
                }

                var serviceProvider = scope.ServiceProvider;
                var loggerFactory = serviceProvider.GetRequiredService<ILoggerFactory>();
                loggerFactory.AddFile("Logs/log-{Date}.txt");
            }
        }
    }
}
