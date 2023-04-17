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


		@GetMapping("/showwishes")
		public String showWishes(Model modelWish, int wishListsId){
			modelWish.addAttribute("showWishes", wishlistRepository.getWishItemsByID(wishListsId));
			return "showwishes";
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
								 @RequestParam("newItemDescription") String newItemDescription,
								 @RequestParam("itemURL") String newItemURL,
								 @RequestParam("itemPrice") double newItemPrice) {
			//Laver nyt ønske
			WishListItems newWish = new WishListItems();
			newWish.setWishListId(newWishListId);
			newWish.setItemLineId(newItemLineId);
			newWish.setItemName(newItemName);
			newWish.setItemQTY(newItemQTY);
			newWish.setItemDescription(newItemDescription);
			newWish.setItemURL(newItemURL);
			newWish.setItemPrice(newItemPrice);
			newWish.setItemReserved(false);
			newWish.setItemReservedBy("");

			//Gem nyt ønske
			wishlistRepository.addWish(newWish);

			return "redirect:showwishes";

		}

		@GetMapping("/updatewish/{itemLineId}")
		public String showUpdateWish(@PathVariable("itemLineId") int updateWish, Model model){

			//Find ønske med itemLineId lig update itemLineId i databasen
			WishListItems updateWishListItems = wishlistRepository.findWishById(updateWish);

			//Tilføj ønske til viewmodel, så det kan bruges i thymeleaf
			model.addAttribute("wishListItem", updateWishListItems);

			//Fortæl Spring hvilken HTML-side der skal vises.
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

			return "/redirect: showwishes";
		}

		@GetMapping("/deletewish/{itemLineId}")
		public String deleteWish(@PathVariable("itemLineId") int deleteWish){
			//slet fra repository
			wishlistRepository.deleteById(deleteWish);

			//returner til index-siden
			return "redirect:showwishes";
		}

		@GetMapping("/login")
		public String showLogin(HttpSession session,Model wishUserModel){
			WishUser user=(WishUser) session.getAttribute("User");
			if(user==null){
				user=new WishUser();
			}
			session.setAttribute("User",user);

			return "/login";
		}

		@PostMapping("/login")
		public String login(@RequestParam("username") String username, @RequestParam("pwd") String password,HttpSession session,Model wishUserModel ){
			WishUser user=(WishUser) session.getAttribute("User");
			user=wishlistRepository.loginUser(user,username,password);
			// the portion below should check if user exist, but may be faulty - or maybe azure isn't running
			if (user.getUserName()==null){
				return "login";
			}
			session.setAttribute("UserID",user.getUserId());
			session.setAttribute("UserName",user.getUserName());
			wishUserModel.addAttribute("username",user.getUserName());

			return "/show_user_page";
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
		@GetMapping("/show_user_page")
		public String userPage(HttpSession session,Model wishUserModel){

			return "/show_user_page";
		}
	}
