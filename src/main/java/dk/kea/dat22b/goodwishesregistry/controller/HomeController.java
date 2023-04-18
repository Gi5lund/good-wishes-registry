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

		public HomeController(WishlistRepository wishlistRepository)
			{
				this.wishlistRepository = wishlistRepository;
			}

		@GetMapping("/")
		public String index()
			{
				return "index";
			}

		@GetMapping("/index")
		public String home()
			{
				return "index";
			}


		@GetMapping("/showwishes")
		public String showWishes(Model modelWish, int wishListsId){
			modelWish.addAttribute("showWishes", wishlistRepository.getWishItemsByID(wishListsId));
			return "showwishes";
		}


		@GetMapping("/createwish")
		public String showCreateWish()
			{
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
		public String showLogin(HttpSession session, Model wishUserModel)
			{
				WishUser user = (WishUser) session.getAttribute("User");
				String loginstatus = "";
				if (session.getAttribute("loginStatus") == null) {
					session.setAttribute("loginStatus", loginstatus);
				}
				if (user == null) {
					user = new WishUser();
				}
				session.setAttribute("User", user);
				session.setAttribute("loginStatus", loginstatus);
				session.setAttribute("wishlists", user.getWishLists());
				wishUserModel.addAttribute("wishlists", user.getWishLists());

				return "/login";
			}

		@PostMapping("/login")
		public String login(@RequestParam("username") String username, @RequestParam("pwd") String password, HttpSession session, Model wishUserModel)
			{
				WishUser user = (WishUser) session.getAttribute("User");
				if (user == null) {
					user = new WishUser();
				}
				String loginstatus = "";
				user = wishlistRepository.loginUser(user, username, password);
				// the portion below should check if user exist,
				if (user.getUserName() == null) {
					loginstatus = "fail";
					session.setAttribute("loginStatus", loginstatus);
					return "login";
				}
				loginstatus = "succes";
				session.setAttribute("UserID", user.getUserId());
				session.setAttribute("UserName", user.getUserName());
				session.setAttribute("loginStatus", loginstatus);

				List<WishList> wishLists = wishlistRepository.getWishListByUserId(user.getUserId());
				session.setAttribute("wishlists", user.getWishLists());

				for (WishList w : wishLists) {
					w.setWisher(wishlistRepository.getWishItemsByID(w.getWishListId()));
				}
				user.setWishLists(wishLists);
				session.setAttribute("wishlists", user.getWishLists());

				wishUserModel.addAttribute("username", user.getUserName());
				wishUserModel.addAttribute("wishlists", user.getWishLists());
				return "show-user-page";
			}


		@GetMapping("/show-user-page")
		public String userPage(HttpSession session, Model wishUserModel) {
				int userID=(int) session.getAttribute("UserID");
				WishUser user=wishlistRepository.getUserById(userID);
				wishUserModel.addAttribute("wishlists",user.getWishLists());

				return "/show-user-page";
		}
		@GetMapping("/createwishlist")
		public String showCreateWishList(){
			return "createwishlist";
		}
		@PostMapping("/create-wishlist")
		public String createWishlist(@RequestParam("wishListId") int newWishListId,
										 @RequestParam("WishListName") String newWishlistName,
										 @RequestParam("occation") String newOccation,
										 @RequestParam("userId") int newUserId){

			WishList newWishList = new WishList();
			newWishList.setWishListId(newWishListId);
			newWishList.setWishListName(newWishlistName);
			newWishList.setOccation(newOccation);
			newWishList.setUserId(newUserId);

			wishlistRepository.addWishList(newWishList,newUserId);

			return "redirect:/show-user-page";
		}
		@GetMapping("/updatewishlist/{wishListId}")
		public String showUpdateWishList(@PathVariable("wishListId") int updateWishlist, Model model)
		{
			WishList updateWishListe = wishlistRepository.findWishListById(updateWishlist);


			model.addAttribute("wishList", updateWishListe);

			return "updatewish";
		}
		@PostMapping("/updatewishlist")
		public String updateWishList(@RequestParam("wishListId") int updateWishListId,
									 @RequestParam("WishListName") String updateWishlistName,
									 @RequestParam("occation") String updateOccation,
									 @RequestParam("userId") int updateUserId){
			WishList updatedWishList = new WishList(updateWishListId, updateWishlistName, updateOccation, updateUserId);

			wishlistRepository.updateWishList(updatedWishList);
			return "redirect:/show-user-page";
		}
		@GetMapping("/deletewishlist/{wishListId}")
		public String deleteWishList(@PathVariable("wishListId") int deleteWishlist) {

			wishlistRepository.deleteWishListId(deleteWishlist);

			return "redirect:/show-user-page";
		}
		@PostMapping("/adduser")
		public String createUser(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword)
			{
				WishUser newUser = new WishUser();
				newUser.setUserName(userName);
				newUser.setUserPassword(userPassword);
				wishlistRepository.addUser(newUser);
				return "redirect:jacob";
			}

		@GetMapping("/adduser")
		public String showAddUser()
			{
				return "/adduser";
			}



	}