package com.springboot.testbackend.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.testbackend.entity.Users;
import com.springboot.testbackend.entity.UsersRoles;
import com.springboot.testbackend.form.UsersForm;
import com.springboot.testbackend.repository.UsersRepository;
import com.springboot.testbackend.repository.UsersRolesRepository;
import com.springboot.testbackend.validator.UsersValidator;

@Controller
public class HomeController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UsersRolesRepository usersRolesRepository;

	@Autowired
	private UsersValidator usersValidator;

	@RequestMapping(value = { "/", "/login" })
	public String login(@RequestParam(required = false) String message, final Model model) {
		if (message != null && !message.isEmpty()) {
			if (message.equals("logout")) {
				model.addAttribute("message", "Logout!");
			}
			if (message.equals("error")) {
				model.addAttribute("message", "Login Failed!");
			}
		}
		return "login";
	}

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {

		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == UsersForm.class) {
			dataBinder.setValidator(usersValidator);
		}
		// ...
	}

	@RequestMapping("/join")
	public String join(Model model) {
		UsersForm userForm = new UsersForm();
		model.addAttribute("userForm", userForm);
		return "join";
	}

	@RequestMapping(value = "/dojoin", method = RequestMethod.POST)
	public String saveRegister(Model model, //
			@ModelAttribute("userForm") @Validated UsersForm userForm, //
			BindingResult result) {
		if (result.hasErrors()) {
			return "join";
		}

		Users user = new Users();
		user.setId(userForm.getId());
		user.setPassword("{noop}" + userForm.getPassword());
		user.setName(userForm.getName());
		user.setEmail(userForm.getEmail());
		user.setPhone(userForm.getPhone());
		user.setAddress(userForm.getAddress());
		user.setEnabled(true);

		UsersRoles userrole = new UsersRoles();
		if (user.getId() == "Admin99") {
			userrole.setRole(1);
		} else {
			userrole.setRole(2);
		}
		userrole.setUser(userForm.getId());

		usersRepository.save(user);
		usersRolesRepository.save(userrole);

		String message = "Join Success";
		model.addAttribute("message", message);
		return "login";
	}

	@RequestMapping("/user")
	public String user() {
		return "user";
	}

	@RequestMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("listUsers", usersRepository.findAll());
		return "admin";
	}

	@RequestMapping("/users-list")
	public String listUsers() {
		return "redirect:admin";
	}

	@RequestMapping("/admin/user-details/{id}")
	public String userDetails(@PathVariable String id, Model model) {
		Optional<Users> user = usersRepository.findById(id);
		if (user.isPresent()) {
			Users subuser = user.get();
			subuser.setPassword(subuser.getPassword().substring(6));
			model.addAttribute("user", subuser);
		}
		return "user-details";
	}

	@RequestMapping("/admin/user-update/{id}")
	public String userUpdate(@PathVariable String id, Model model) {
		Optional<Users> user = usersRepository.findById(id);
		if (user.isPresent()) {
			Users subuser = user.get();
			subuser.setPassword(subuser.getPassword().substring(6));
			model.addAttribute("user", subuser);
		}
		return "user-update";
	}

	@RequestMapping("/admin/updateUser")
	public String updateUser(@ModelAttribute("user") Users user, Model model) {
		user.setPassword("{noop}" + user.getPassword());
		usersRepository.save(user);
		model.addAttribute("listUsers", usersRepository.findAll());
		return "admin";
	}

	@RequestMapping("/admin/user-delete/{id}")
	public String deleteUser(@PathVariable String id, Model model) {
		usersRepository.deleteById(id);
		model.addAttribute("listUsers", usersRepository.findAll());
		return "admin";
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}

}
