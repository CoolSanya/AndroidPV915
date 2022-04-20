using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Shop.Web.Constants;
using Shop.Web.Data.Entities.Identity;

namespace Shop.Web.Data
{
    public static class SeederDB
    {
        public static void SeedData(this IApplicationBuilder app)
        {
            using (var scope =
                app.ApplicationServices.GetRequiredService<IServiceScopeFactory>().CreateScope())
            {
                var logger = scope.ServiceProvider.GetRequiredService<ILogger<Program>>();
                try
                {
                    logger.LogInformation("Migrate DB DAtabases");
                    var context = scope.ServiceProvider.GetRequiredService<AppEFContext>();
                    context.Database.Migrate();
                    logger.LogInformation("Seeding Web And Localization Databases");
                    InitRoleAndUsers(scope);
                }
                catch (Exception ex)
                {
                    logger.LogError("Problem seed database " + ex.Message);

                }
            }
        }

        private static void InitRoleAndUsers(IServiceScope scope)
        {
            var roleManager = scope.ServiceProvider.GetRequiredService<RoleManager<AppRole>>();
            var userManager = scope.ServiceProvider.GetRequiredService<UserManager<AppUser>>();
            if (!roleManager.Roles.Any())
            {
                var logger = scope.ServiceProvider.GetRequiredService<ILogger<Program>>();
                var result = roleManager.CreateAsync(new AppRole
                {
                    Name = Roles.Admin
                }).Result;
                if (result.Succeeded)
                    logger.LogWarning($"Create role {Roles.Admin}");
                else
                    logger.LogError($"Problem crate role {Roles.Admin}");
                result = roleManager.CreateAsync(new AppRole
                {
                    Name = Roles.User
                }).Result;
            }

            if (!userManager.Users.Any())
            {
                var logger = scope.ServiceProvider.GetRequiredService<ILogger<RoleManager<AppUser>>>();
                string email = "admin@gmail.com";
                var user = new AppUser
                {
                    Email = email,
                    UserName = email,
                    FirstName = "Петро",
                    SecondName = "Шпрот",
                    PhoneNumber = "+38(098)232 34 22",
                    Photo = "1.jpg"
                };
                var result = userManager.CreateAsync(user, "12345").Result;
                if (result.Succeeded)
                {
                    logger.LogWarning("Create user " + user.UserName);
                    result = userManager.AddToRoleAsync(user, Roles.Admin).Result;
                }
                else
                {
                    logger.LogError("Faild create user " + user.UserName);
                }
            }
        }
    }
}
