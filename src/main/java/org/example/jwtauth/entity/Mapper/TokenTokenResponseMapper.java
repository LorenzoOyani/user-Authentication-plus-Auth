package org.example.jwtauth.entity.Mapper;

import org.example.jwtauth.entity.Token;
import org.example.jwtauth.payload.TokenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenTokenResponseMapper extends BaseMapper<Token, TokenResponse> {

    TokenResponse map(Token token);

     static TokenTokenResponseMapper init(){
        return Mappers.getMapper(TokenTokenResponseMapper.class);
    }
}
