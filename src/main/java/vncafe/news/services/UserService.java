package vncafe.news.services;

import java.security.KeyPair;
import lombok.RequiredArgsConstructor;
import java.util.Set;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vncafe.news.entities.User;
import vncafe.news.exceptions.BabRequestError;
import vncafe.news.exceptions.DuplicateRecordError;
import vncafe.news.exceptions.NotFoundError;
import vncafe.news.models.request.LoginReq;
import vncafe.news.models.request.RegisterReq;
import vncafe.news.models.request.UpdateUserReq;
import vncafe.news.models.response.LoginRes;
import vncafe.news.models.response.LoginRes.TokenStore;
import vncafe.news.repositories.UserRepo;
import vncafe.news.security.jwt.JwtService;
import vncafe.news.utils._enum.UserRoleEnum;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class UserService {
  final private UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final KeyTokenService keyTokenService;

  public LoginRes registerUserLocal(RegisterReq registerReq, Boolean isOwner) {
    // check email
    if (userRepo.existsByEmail(registerReq.getEmail()))
      throw new BabRequestError("user is registered");

    User user = userRepo.save(User.builder()
        .name(registerReq.getName())
        .email(registerReq.getEmail())
        .roles(isOwner ? Set.of(UserRoleEnum.OWNER, UserRoleEnum.USER) : Set.of(UserRoleEnum.USER))
        .password(passwordEncoder.encode(registerReq.getPassword()))
        .build());

    KeyPair keys = JwtService.generatorKeyPair();

    TokenStore tokens = new TokenStore(jwtService.createAccessToken(user.getEmail(), keys.getPrivate()),
        jwtService.createRefreshToken(user.getEmail(), keys.getPrivate()));

    if (!keyTokenService.createKeyStore(
        user.getId(),
        jwtService.getStringFromPublicKey(keys.getPublic()),
        tokens.getRefreshToken()))
      throw new RuntimeException("fail to create keyStore");

    return new LoginRes(tokens, user);
  }

  public LoginRes loginLocal(LoginReq loginReq) {
    // check email password
    User foundShop = userRepo.findByEmail(loginReq.getEmail())
        .orElseThrow(() -> new NotFoundError("user is not registered"));

    if (!passwordEncoder.matches(loginReq.getPassword(), foundShop.getPassword()))
      throw new BabRequestError("password is not true");

    KeyPair keys = JwtService.generatorKeyPair();

    TokenStore tokens = new TokenStore(jwtService.createAccessToken(loginReq.getEmail(), keys.getPrivate()),
        jwtService.createRefreshToken(loginReq.getEmail(), keys.getPrivate()));

    if (!keyTokenService.createKeyStore(
        foundShop.getId(),
        jwtService.getStringFromPublicKey(keys.getPublic()),
        tokens.getRefreshToken()))
      throw new RuntimeException("fail to create keyStore");
    return new LoginRes(tokens, foundShop);
  }

  public User updateUser(String id, UpdateUserReq user) {
    System.out.println(user);
    User foundUser = userRepo.findById(id).orElseThrow(() -> new NotFoundError("id", id));

    if (userRepo.existsByNameAndIdNot(user.getName(), id))
      throw new DuplicateRecordError("name", user.getName());

    if (userRepo.existsByEmailAndIdNot(user.getEmail(), id))
      throw new DuplicateRecordError("email", user.getEmail());

    foundUser.setEmail(user.getEmail());
    foundUser.setName(user.getName());
    foundUser.setImage(user.getImage());

    return userRepo.save(foundUser);

  }
}
