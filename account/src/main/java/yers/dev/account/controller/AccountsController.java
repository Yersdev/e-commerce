package yers.dev.account.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@Controller
@AllArgsConstructor

@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsController {
    private final AccountsService accountsService;

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);



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

    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto> fetchAccountDetails(@Valid @RequestBody AccountsDto accountsDto) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }

}
