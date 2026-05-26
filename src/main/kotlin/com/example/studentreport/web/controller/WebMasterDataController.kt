package com.example.studentreport.web.controller

import com.example.studentreport.web.service.MasterDataService
import com.example.studentreport.web.service.WebAuthHelper
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/master-data")
class WebMasterDataController(
    private val masterDataService: MasterDataService,
    private val webAuthHelper: WebAuthHelper
) {
    @GetMapping
    fun masterData(auth: Authentication?, model: Model): String {
        if (!webAuthHelper.isAdmin(auth)) {
            return "redirect:/dashboard"
        }

        model.addAttribute("isAdmin", true)
        model.addAttribute("categories", masterDataService.getAllCategories())
        model.addAttribute("rooms", masterDataService.getAllRooms())

        return "master_data"
    }

    @PostMapping("/categories")
    fun addCategory(
        @RequestParam name: String,
        @RequestParam description: String
    ): String {
        masterDataService.addCategory(name, description)
        return "redirect:/master-data"
    }
}