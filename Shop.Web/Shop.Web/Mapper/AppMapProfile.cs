using AutoMapper;
using Shop.Web.Data.Entities.Identity;
using Shop.Web.Models;

namespace Shop.Web.Mapper
{
    public class AppMapProfile : Profile
    {
        public AppMapProfile()
        {
            CreateMap<RegisterViewModel, AppUser>()
                .ForMember(x => x.Photo, opt => opt.Ignore())
                .ForMember(x => x.PhoneNumber, opt => opt.MapFrom(x => x.Phone))
                .ForMember(x => x.UserName, opt => opt.MapFrom(x => x.Email));

            CreateMap<AppUser, UserItemViewModel>()
                .ForMember(x => x.Photo, opt => opt.MapFrom(x => $"/images/{x.Photo}"))
                .ForMember(x => x.Phone, opt => opt.MapFrom(x => x.PhoneNumber));
        }
    }
}
