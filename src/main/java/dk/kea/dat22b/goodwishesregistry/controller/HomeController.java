package dk.kea.dat22b.goodwishesregistry.controller;

import dk.kea.dat22b.goodwishesregistry.model.WishList;
import dk.kea.dat22b.goodwishesregistry.model.WishListItems;
import dk.kea.dat22b.goodwishesregistry.model.WishUser;
import dk.kea.dat22b.goodwishesregistry.repository.WishlistRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController
	{
		WishlistRepository wishlistRepository;
		public HomeController(WishlistRepository wishlistRepository){
			this.wishlistRepository = wishlistRepository;
		}
		@GetMapping("/")
		public String index(){
			return "index";
		}
		@GetMapping("/index")
		public String home(){
			return "index";
		}

		@GetMapping("/createwish")
		public String showCreateWish(){
			return "createwish";
		}

		@PostMapping("/createwish")
		public String createWish(@RequestParam("wishListId") int newWishListId,
								 @RequestParam("itemLineId") int newItemLineId,
								 @RequestParam("itemName") String newItemName,
								 @RequestParam("itemQTY") int newItemQTY,
								 @RequestParam("itemDescreption") String newItemDescreption,
								 @RequestParam("itemURL") String newItemURL,
								 @RequestParam("itemPrice") double newItemPrice) {
			//Laver nyt ønske
			WishListItems newWish = new WishListItems();
			newWish.setWishListId(newWishListId);
			newWish.setItemLineId(newItemLineId);
			newWish.setItemName(newItemName);
			newWish.setItemQTY(newItemQTY);
			newWish.setItemDescription(newItemDescreption);
			newWish.setItemURL(newItemURL);
			newWish.setItemPrice(newItemPrice);
			newWish.setItemReserved(false);
			newWish.setItemReservedBy("");

			//Gem nyt ønske
			wishlistRepository.addWish(newWish);

			return "redirect:/";

		}

		@GetMapping("/updatewish/{itemListId}")
		public String showUpdateWish(@PathVariable("itemLineId") int updateWish, Model model){

			WishListItems updateWishListItems = wishlistRepository.findWishById(updateWish);

			model.addAttribute("wishListItem", updateWishListItems);

			return "updatewish";
		}

		@PostMapping("/updatewish")
		public String updateWishItem(@RequestParam("wishListId") int updateWishListId,
									 @RequestParam("itemLineId") int updateItemLineId,
								 	 @RequestParam("itemName") String updateItemName,
								 	 @RequestParam("itemQTY") int updateItemQTY,
									 @RequestParam("itemDescreption") String updateItemDescreption,
									 @RequestParam("itemURL") String updateItemURL,
									 @RequestParam("itemPrice") double updateItemPrice,
									 @RequestParam("itemReserved") boolean updateItemReserved,
									 @RequestParam("itemReservedBy") String updateItemReservedBy){

			WishListItems updateWishItem = new WishListItems(updateWishListId, updateItemLineId, updateItemName, updateItemQTY, updateItemDescreption,
															updateItemURL, 	updateItemPrice, updateItemReserved,updateItemReservedBy);

			wishlistRepository.updateWish(updateWishItem);

			return "/redirect:wishlist";
		}

		@GetMapping("/delete/{itemLineId}")
		public String deleteWish(@PathVariable("itemLineId") int deleteWish){
			//slet fra repository
			wishlistRepository.deleteById(deleteWish);

			//returner til index-siden
			return "redirect:wishlist";
		}

		@GetMapping("/login")
		public String showLogin(HttpSession session,Model wishUserModel){
			WishUser user=(WishUser) session.getAttribute("User");
			String loginstatus="";
			if(session.getAttribute("loginStatus")==null){
				session.setAttribute("loginStatus",loginstatus);
			}
			if(user==null){
				user=new WishUser();
			}
			session.setAttribute("User",user);
			session.setAttribute("loginStatus",loginstatus);

			return "/login";
		}

		@PostMapping("/login")
		public String login(@RequestParam("username") String username, @RequestParam("pwd") String password,HttpSession session,Model wishUserModel ){
			WishUser user=(WishUser) session.getAttribute("User");
			String loginstatus="";
			user=wishlistRepository.loginUser(user,username,password);
			// the portion below should check if user exist, but may be faulty - or maybe azure isn't running
			if (user.getUserName()==null){
				loginstatus="fail";
				session.setAttribute("loginStatus",loginstatus);
				return "login";
			}
			loginstatus="succes";
			session.setAttribute("UserID",user.getUserId());
			session.setAttribute("UserName",user.getUserName());
			session.setAttribute("loginStatus",loginstatus);
			wishUserModel.addAttribute("username",user.getUserName());

			return "show-user-page";
		}
		@GetMapping("/jacob")
		public String login(){
			return "jacob";
		}
		@PostMapping("/adduser")
		public String createUser(@RequestParam("userName") String userName,@RequestParam("userPassword") String userPassword){
			WishUser newUser=new WishUser();
			newUser.setUserName(userName);
			newUser.setUserPassword(userPassword);
			wishlistRepository.addUser(newUser);
			return "redirect:jacob";
		}

		@GetMapping("/adduser")
		public String showAddUser(){
			return "/adduser";
		}
//		@GetMapping("/show_user_page")
//		public String userPage(HttpSession session,Model wishListModel){
//			WishList wl=wishlistRepository.findWishListById()
//			int userid= (int) session.getAttribute("UserID");
//			WishUser user=wishlistRepository.getUserById(userid);
//			List<WishList> wishListList=new ArrayList<>();
//			wishListList=wishlistRepository.getWishListByUserId(userid);
//
//				user.setWishLists(wishListList);
//				session.setAttribute("wishlists",wishListList);
//
//			wishListModel.addAttribute("wishlists",wishListList);
//			return "/show_user_page";
//		}
		@GetMapping("/show-user-page")
		public String userPage(Model wishListModel){
			int userid=1;
			List<WishList> wl=wishlistRepository.getWishListByUserId(userid);
//			List<WishList> wishListList=new ArrayList<>();
//			wishListList=wishlistRepository.getWishListByUserId(userid);
//
//			user.setWishLists(wishListList);
//			session.setAttribute("wishlists",wishListList);

			wishListModel.addAttribute("wishlists",wl);
			return "show-user-page";}
	}
