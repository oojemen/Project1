package com.revature.controllers;

import com.revature.daos.ExpenseTypeDAO;
import com.revature.daos.StatusDAO;
import com.revature.models.Reimbursement;
import com.revature.models.Status;
import com.revature.models.User;
import com.revature.security.JwtGenerator;
import com.revature.services.ReimbursementService;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class ReimbursementController {
    private final ReimbursementService reimbService;
    private final UserService userService;
    private final StatusDAO statusDAO;
    private final ExpenseTypeDAO expenseTypeDAO;
    private final JwtGenerator tokenGenerator;

    @Autowired
    public ReimbursementController(ReimbursementService reimbService,
                                   UserService userService, StatusDAO statusDAO,
                                   ExpenseTypeDAO expenseTypeDAO,
                                   JwtGenerator tokenGenerator) {
        this.reimbService = reimbService;
        this.userService = userService;
        this.statusDAO = statusDAO;
        this.expenseTypeDAO = expenseTypeDAO;
        this.tokenGenerator = tokenGenerator;
    }

    @GetMapping("/employee/reimbursements")
    public ResponseEntity<?> getAllUserReimbursementsHandler(
            @RequestHeader("Authorization") String token) {
        String username = tokenGenerator.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (token.isEmpty() || user == null) {
            return new ResponseEntity<>("You must be logged in",
                    HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(reimbService.getAllUserReimbursements(user),
                HttpStatus.OK);
    }

    @GetMapping("/employee/reimbursements/pending")
    public ResponseEntity<?> getUserPendingReimbursementsHandler(
            @RequestHeader("Authorization") String token) {
        String username = tokenGenerator.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (token.isEmpty() || user == null) {
            return new ResponseEntity<>("You must be logged in",
                    HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(reimbService.getUserReimbursementsByStatus(
                user,
                statusDAO.findByName("Pending")
        ), HttpStatus.OK);
    }

    @GetMapping("/employee/reimbursements/approved")
    public ResponseEntity<?> getUserApprovedReimbursementsHandler(
            @RequestHeader("Authorization") String token) {
        String username = tokenGenerator.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (token.isEmpty() || user == null) {
            return new ResponseEntity<>("You must be logged in",
                    HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(reimbService.getUserReimbursementsByStatus(
                user,
                statusDAO.findByName("Approved")
        ), HttpStatus.OK);
    }

    @GetMapping("/employee/reimbursements/denied")
    public ResponseEntity<?> getUserDeniedReimbursementsHandler(
            @RequestHeader("Authorization") String token) {
        String username = tokenGenerator.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (token.isEmpty() || user == null) {
            return new ResponseEntity<>("You must be logged in",
                    HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(reimbService.getUserReimbursementsByStatus(
                user,
                statusDAO.findByName("Denied")
        ), HttpStatus.OK);
    }

    @GetMapping("/reimbursements")
    public ResponseEntity<?> getAllReimbursementsHandler(
            @RequestParam(value = "id", required = false) Integer uid) {
        if (uid != null && uid > 0) {
            return new ResponseEntity<>(
                    reimbService.getAllUserReimbursements(userService.getUserById(uid)),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(reimbService.getAllReimbursements(),
                HttpStatus.OK);
    }

    @GetMapping("/reimbursements/pending")
    public ResponseEntity<?> getAllPendingReimbursementsHandler(
            @RequestParam(name = "id", required = false) Integer uid) {
        Status status = statusDAO.findByName("Pending");

        if (uid != null && uid > 0) {
            return new ResponseEntity<>(
                    reimbService.getUserReimbursementsByStatus(
                            userService.getUserById(uid),
                            status
                    ), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                reimbService.getAllReimbursementsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/reimbursements/approved")
    public ResponseEntity<?> getAllApprovedReimbursementsHandler(
            @RequestParam(name = "id", required = false) Integer uid) {
        Status status = statusDAO.findByName("Approved");

        if (uid != null && uid > 0) {
            return new ResponseEntity<>(
                    reimbService.getUserReimbursementsByStatus(
                            userService.getUserById(uid),
                            status
                    ), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                reimbService.getAllReimbursementsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/reimbursements/denied")
    public ResponseEntity<?> getAllDeniedReimbursementsHandler(
            @RequestParam(name = "id", required = false) Integer uid) {
        Status status = statusDAO.findByName("Denied");

        if (uid != null && uid > 0) {
            return new ResponseEntity<>(
                    reimbService.getUserReimbursementsByStatus(
                            userService.getUserById(uid),
                            status
                    ), HttpStatus.OK);
        }

        return new ResponseEntity<>(
                reimbService.getAllReimbursementsByStatus(status), HttpStatus.OK);
    }

    @GetMapping("/reimbursements/travel")
    public ResponseEntity<?> getAllTravelReimbursementsHandler(
            @RequestParam(name = "id", required = false) Integer uid) {
        if (uid != null && uid > 0) {
            return new ResponseEntity<>(
                    reimbService.getUserReimbursementsByType(
                            userService.getUserById(uid),
                            expenseTypeDAO.findByType("Travel")
                    ), HttpStatus.OK);
        }

        return new ResponseEntity<>(reimbService.getAllReimbursementsByType(
                expenseTypeDAO.findByType("Travel")
        ), HttpStatus.OK);
    }

    @GetMapping("/reimbursements/lodging")
    public ResponseEntity<?> getAllLodgingReimbursementsHandler(
            @RequestParam(name = "id", required = false) Integer uid) {
        if (uid != null && uid > 0) {
            return new ResponseEntity<>(
                    reimbService.getUserReimbursementsByType(
                            userService.getUserById(uid),
                            expenseTypeDAO.findByType("Lodging")
                    ), HttpStatus.OK);
        }

        return new ResponseEntity<>(reimbService.getAllReimbursementsByType(
                expenseTypeDAO.findByType("Lodging")
        ), HttpStatus.OK);
    }

    @GetMapping("/reimbursements/food")
    public ResponseEntity<?> getAllFoodReimbursementsHandler(
            @RequestParam(name = "id", required = false) Integer uid) {
        if (uid != null && uid > 0) {
            return new ResponseEntity<>(reimbService.getUserReimbursementsByType(
                    userService.getUserById(uid),
                    expenseTypeDAO.findByType("Food")
            ), HttpStatus.OK);
        }
        return new ResponseEntity<>(reimbService.getAllReimbursementsByType(
                expenseTypeDAO.findByType("Food")
        ), HttpStatus.OK);
    }

    @GetMapping("/reimbursements/other")
    public ResponseEntity<?> getAllOtherReimbursementsHandler() {
        return new ResponseEntity<>(reimbService.getAllReimbursementsByType(
                expenseTypeDAO.findByType("Other")
        ), HttpStatus.OK);
    }

    @PostMapping("/employee/reimbursements/new")
    public ResponseEntity<?> createReimbursementHandler(
            @RequestBody Reimbursement r,
            @RequestHeader("Authorization") String token) {
        String username = tokenGenerator.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (token.isEmpty() || user == null) {
            return new ResponseEntity<>("You must be logged in",
                    HttpStatus.FORBIDDEN);
        }

        r.setUser(user);

        return new ResponseEntity<>(reimbService.createReimbursement(
                user.getId(),
                r), HttpStatus.CREATED);
    }

    @PutMapping("/reimbursements/status-update")
    public ResponseEntity<?> updateReimbursementStatus(
            @RequestBody Reimbursement r) {
        return new ResponseEntity<>(reimbService.updateReimbursementStatus(r),
                HttpStatus.OK);
    }
}