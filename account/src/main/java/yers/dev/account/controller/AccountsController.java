package yers.dev.account.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yers.dev.account.constants.AccountsConstants;
import yers.dev.account.dto.ResponseDto;
import yers.dev.account.dto.AccountsDto;
import yers.dev.account.service.AccountsService;

import java.util.List;

@AllArgsConstructor
@RequestMapping(path="/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RestController
public class AccountsController {
    private final AccountsService accountsService;



    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody AccountsDto accountsDto) {
        accountsService.createNewAccount(accountsDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody AccountsDto accountsDto) {
        boolean isUpdated = accountsService.updateUser(accountsDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@Valid @RequestBody AccountsDto accountsDto) {
        accountsService.deleteUserById(accountsDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_DELETED_200, AccountsConstants.MESSAGE_DELETED_200));
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<AccountsDto> fetchAccountDetailsById(@PathVariable("id") Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsService.getUserById(id));
    }

    @GetMapping("/fetch/email/{email}")
    public ResponseEntity<AccountsDto> fetchAccountDetailsByEmail(@PathVariable("email") String email) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsService.getUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<AccountsDto>> getAllAccounts() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsService.getAllUsers());
    }
}
