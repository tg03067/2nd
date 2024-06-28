package org.example.second.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.second.common.AppProperties;
import org.example.second.common.CookieUtils;
import org.example.second.common.CustomFileUtils;
import org.example.second.security.AuthenticationFacade;
import org.example.second.security.jwt.JwtTokenProviderV2;
import org.example.second.user.model.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParentsUserServiceImpl implements ParentsUserService {
    private final ParentsUserMapper mapper;
    private final CustomFileUtils customFileUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProviderV2 jwtTokenProvider;
    private final CookieUtils cookieUtils;
    private final AuthenticationFacade authenticationFacade;
    private final AppProperties appProperties;
    private final String ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{6,12}$";
    private final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,20}$";
    private final Pattern idPattern = Pattern.compile(ID_PATTERN);
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private final String NAME_PATTERN  = "^[가-힣a-zA-Z\\s-]+$";
    private final Pattern namePattern = Pattern.compile(NAME_PATTERN);


    @Override
    public int postParentsUser(PostParentsUserReq p) {

        if(!idPattern.matcher(p.getUid()).matches()){
            throw new RuntimeException("아이디 이상");
        } else if (!passwordPattern.matcher(p.getUpw()).matches()) {
            throw new RuntimeException("비밀번호 이상");
        } else if (!emailPattern.matcher(p.getEmail()).matches()) {
            throw new RuntimeException("Email 이상");
        }
        String password = passwordEncoder.encode(p.getUpw());
        p.setUpw(password);
        return mapper.postParentsUser(p);
    }

    @Override
    public ParentsUserEntity getParentsUser(GetParentsUserReq parentsId) {
        return mapper.getParentsUser(parentsId);
    }

    @Override
    public int patchParentsUser(PatchParentsUserReq p) {
        return mapper.patchParentsUser(p);
    }

    @Override
    public GetFindIdRes getFindId(GetFindIdReq req) {
        return mapper.getFindId(req);
    }

    @Override
    public int PatchPassword(PatchPasswordReq req) {
        GetParentsUserReq req1 = new GetParentsUserReq();
        req1.setSignedUserId(req.getParentsId());
        ParentsUserEntity entity = mapper.getParentsUser(req1);
        if(Objects.isNull(entity)){
            throw new RuntimeException("아이디를 확인해 주세요");
        } else if(!BCrypt.checkpw(req.getUpw(), entity.getUpw())) {
            throw new RuntimeException("비밀번호를 확인해 주세요");
        }
        String password = passwordEncoder.encode(req.getNewUpw());
        req.setNewUpw(password);
        req.setParentsId(entity.getParentsId());
        return mapper.PatchPassword(req);
    }
}
