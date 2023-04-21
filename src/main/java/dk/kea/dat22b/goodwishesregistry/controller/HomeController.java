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


		@GetMapping("/showwishes/{wishlistid}")
		public String showWishes(@PathVariable("wishlistid") int wishListsId,Model modelWish,HttpSession session){
			modelWish.addAttribute("showWishes", wishlistRepository.getWishItemsByID(wishListsId));
			session.setAttribute("currentWishList",wishListsId);
			return "showwishes";
		}


		@GetMapping("/createwish")
		public String showCreateWish()
			{
				return "createwish";
			}


		@PostMapping("/createwish")
		public String createWish(@RequestParam("itemName") String newItemName,
								 @RequestParam("itemQTY") int newItemQTY,
								 @RequestParam("itemDescription") String newItemDescription,
								 @RequestParam("itemURL") String newItemURL,
								 @RequestParam("itemPrice") double newItemPrice,HttpSession session) {
			//Laver nyt ønske med autogenereret itemLineId
			int newWishListId=(int) session.getAttribute("currentWishList");
			WishListItems newWish = new WishListItems();
			newWish.setWishListId(newWishListId);
			//newWish.setItemLineId(newItemLineId);
			newWish.setItemName(newItemName);
			newWish.setItemQTY(newItemQTY);
			newWish.setItemDescription(newItemDescription);
			newWish.setItemURL(newItemURL);
			newWish.setItemPrice(newItemPrice);
			newWish.setItemReserved(false); // to be implemented later - default dummy-values to please constructor
			newWish.setItemReservedBy(""); // to be implemented later - default dummy-values to please constructor

			//Gem nyt ønske
			wishlistRepository.addWish(newWish,newWishListId);
			//fortæl hvilken wishlist side du skal tilbage til

			return "redirect:showwishes/"+newWishListId; //bemærk at der IKKE er mellemrum eller / efter : !!!

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

		@PostMapping("updatewish")
		public String updateWishItem(@RequestParam("wishListId") int updateWishListId,
									 @RequestParam("itemLineId") int updateItemLineId,
								 	 @RequestParam("itemName") String updateItemName,
								 	 @RequestParam("itemQTY") int updateItemQTY,
									 @RequestParam("itemDescription") String updateItemDescription,
									 @RequestParam("itemURL") String updateItemURL,
									 @RequestParam("itemPrice") double updateItemPrice,
									 @RequestParam("itemReserved") boolean updateItemReserved,
									 @RequestParam("itemReservedBy") String updateItemReservedBy,Model wishListModel, HttpSession session){

			WishListItems updateWishItem =new WishListItems(updateWishListId,updateItemLineId,updateItemName,updateItemQTY,updateItemDescription,updateItemURL,updateItemPrice,updateItemReserved,updateItemReservedBy);

			wishlistRepository.updateWish(updateWishItem);
			int wishlistid=(int) session.getAttribute("currentWishList");
			wishListModel.addAttribute("showWishes", wishlistid);

			return "redirect:showwishes/"+wishlistid;
		}

		@GetMapping("/deletewish/{itemLineId}")
		public String deleteWish(@PathVariable("itemLineId") int deleteWish, HttpSession session){
			//slet fra repository
			wishlistRepository.deleteById(deleteWish);
			int wishlistid=(int) session.getAttribute("currentWishList");
			//returner til index-siden
			return "redirect:/showwishes/"+wishlistid;
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

				return "login";
			}

		@PostMapping("/login")
		public String login(@RequestParam("username") String username, @RequestParam("pwd") String password, HttpSession session, Model wishUserModel)
			{
				WishUser user = (WishUser) session.getAttribute("User");
				if (user == null) {
					user = new WishUser();
				}
				String loginstatus;
				user = wishlistRepository.loginUser( username, password);
				// the portion below should check if user exist,
				if (user.getUserName() == null) {
					loginstatus = "fail";
					session.setAttribute("loginStatus", loginstatus);
					return "/login";
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
				return "redirect:show-user-page/"+user.getUserId();
			}


		@GetMapping("show-user-page/{id}")
		public String userPage(@PathVariable("id") int userID, HttpSession session, Model wishUserModel) {
//				int userid=(int) session.getAttribute("UserID");
				WishUser user=wishlistRepository.getUserById(userID);
				user.setWishLists( wishlistRepository.getWishListByUserId(user.getUserId()));
			session.setAttribute("wishlists",user.getWishLists());
				wishUserModel.addAttribute("wishlists",user.getWishLists());

				return "show-user-page";
		}
		@GetMapping("/createwishlist")
		public String showCreateWishList(){
			return "createwishlist";
		}
		@PostMapping("/createwishlist")
		public String createWishlist( @RequestParam("WishListName") String newWishlistName,
										 @RequestParam("occation") String newOccation, HttpSession session){

			WishList newWishList = new WishList();
			int newUserId=(int) session.getAttribute("UserID");
			newWishList.setWishListName(newWishlistName);
			newWishList.setOccation(newOccation);
			newWishList.setUserId(newUserId);

			wishlistRepository.addWishList(newWishList,newUserId);
			List<WishList> wishLists= wishlistRepository.getWishListByUserId(newUserId);
			WishList current=wishLists.get(wishLists.size()-1);
			session.setAttribute("currentWishList",current.getWishListId());

			return "redirect:createwish";
		}
		@GetMapping("/updatewishlist/{wishListId}")
		public String showUpdateWishList(@PathVariable("wishListId") int wishlistID, Model wishListModel,HttpSession session)
		{
			session.setAttribute("currentWishList",wishlistID);
			WishList updatewishList = wishlistRepository.findWishListById(wishlistID);


			wishListModel.addAttribute("wishList", updatewishList);

			return "updatewishlist";
		}
		@PostMapping("updatewishlist")
		public String updateWishList( @RequestParam("wishListId") int updateWishListId,@RequestParam("WishListName") String updateWishlistName,
									 @RequestParam("occation") String updateOccation, @RequestParam("userId") int updateUserId, HttpSession session, Model wishUserModel){
			  session.setAttribute("currentWishList",updateWishListId);
//			 session.setAttribute("UserID",updateUserId);
			WishList updatedWishList = new WishList(updateWishListId, updateWishlistName, updateOccation, updateUserId);

			wishlistRepository.updateWishList(updatedWishList);
			WishUser user=wishlistRepository.getUserById(updateUserId);
			user.setWishLists( wishlistRepository.getWishListByUserId(user.getUserId()));
			wishUserModel.addAttribute("wishlists",user.getWishLists());
			session.setAttribute("wishlists",user.getWishLists());
			session.setAttribute("UserID",user.getUserId());
			wishUserModel.addAttribute("username", user.getUserName());
			wishUserModel.addAttribute("wishlists", user.getWishLists());
			return "redirect:show-user-page/"+updateUserId;
		}
		@GetMapping("/deletewishlist/{wishListId}")
		public String deleteWishList(@PathVariable("wishListId") int deleteWishlist, HttpSession session, Model model) {

			wishlistRepository.deleteWishListItemsId(deleteWishlist);
			wishlistRepository.deleteWishListId(deleteWishlist);
			int userid=(int)session.getAttribute("UserID");
			session.setAttribute("wishlists",wishlistRepository.getWishListByUserId(userid));
			model.addAttribute("wishlists", session.getAttribute("wishlists"));
			return "redirect:/show-user-page/"+userid;
		}
		@PostMapping("/adduser")
		public String createUser(@RequestParam("userName") String userName, @RequestParam("userPassword") String userPassword)
			{
				WishUser newUser = new WishUser();
				newUser.setUserName(userName);
				newUser.setUserPassword(userPassword);
				wishlistRepository.addUser(newUser);
				return "redirect:index";
			}

		@GetMapping("/adduser")
		public String showAddUser()
			{
				return "/adduser";
			}





	}