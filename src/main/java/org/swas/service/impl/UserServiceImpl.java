package org.swas.service.impl;

import org.swas.util.DigestUtil;
import org.swas.util.MessagesHelper;
import org.swas.util.SystemParameters;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swas.dao.GenericDao;
import org.swas.dao.UserDao;
import org.swas.domain.User;
import org.swas.domain.UserStatus;
import org.swas.service.ActivationUrlGenerator;
import org.swas.service.Result;
import org.swas.service.TmplMerger;
import org.swas.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 09.11.11
 */
@Service
public class UserServiceImpl extends GenericDaoServiceImpl<User, Long> implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(GenericDao<User, Long> dao) {
        super(dao);
    }

    @Autowired
    private MessagesHelper messagesHelper;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TmplMerger tmplMerger;
    @Autowired
    private UserDao dao;

    @Transactional
    public Long create(User user) {
        return dao.create(user);
    }


    private void sendActivationEmail(User user, String activationUrl, Locale locale) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());

        //WARNING принудительно вызываем установка заголовка письма из конфига, иначе письмо без отравителя
        // возможно баг JavaMail API
        message.setFrom();


        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        model.put("url", activationUrl);
        String text = tmplMerger.mergeTemplate(TmplMerger.TMPL_USER_ACTIVATION, model, locale);
        helper.setText(text, true);
        helper.setSubject(messagesHelper.getMessage("email.ActivationMessage.subject", locale));
        mailSender.send(message);
    }

    private void sendPasswordRestoreEmail(User user, Locale locale) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        String text = tmplMerger.mergeTemplate(TmplMerger.TMPL_USER_PASSWORD_RESTORE, model, locale);
        helper.setText(text, true);
        helper.setSubject(messagesHelper.getMessage("email.PasswordRestore.subject", locale));
        mailSender.send(message);
    }

    @Transactional
    public Result update(User trans) {
        if (trans == null)
            return new Result(new IllegalArgumentException());
        User stored = dao.get(trans.getId());
        if (stored == null)
            return new Result(Result.ERROR_GENERAL, "User with id='" + trans.getId() + "' not found");
        if (StringUtils.isNotBlank(trans.getTimezone()))
            stored.setTimezone(trans.getTimezone());
        if (StringUtils.isNotBlank(trans.getName()))
            stored.setName(trans.getName());
        dao.update(stored);
        return new Result(stored);
    }

    @Transactional
    public Result register(User user, Locale locale, ActivationUrlGenerator activationUrlGenerator) {
        if (user == null || activationUrlGenerator == null)
            return new Result(new IllegalArgumentException());
        user.setStatus(UserStatus.NEW);
        StringBuilder userDataString = new StringBuilder();
        userDataString.append(user.getLogin()).append(user.getPsw()).append(user.getEmail());
        user.setActivationCode(DigestUtil.getSha256Digest(userDataString.toString()));

        if (StringUtils.isEmpty(user.getTimezone())) {
            user.setTimezone(TimeZone.getDefault().getID());

        } else {
            user.setTimezone(TimeZone.getTimeZone(user.getTimezone()).getID());
        }

        dao.create(user);
        if (!"1".equals(SystemParameters.getParam("testMode.userActivation"))) {
            try {
                sendActivationEmail(user, activationUrlGenerator.generateUrl(user), locale);
            } catch (MessagingException e) {
                //!! TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("Error during send activation message:", e);
                return new Result(e);
            }
        } else {
            user.setStatus(UserStatus.ACTIVE);
            dao.update(user);
        }
        return new Result(user);

    }

    @Transactional
    public Result activate(String activationCode) {
        if (StringUtils.isBlank(activationCode))
            return new Result(USER_NOT_FOUND);
        User u = dao.findByActivationCode(activationCode);
        if (u == null)
            return new Result(USER_NOT_FOUND);
        if (UserStatus.ACTIVE.equals(u.getStatus()))
            return new Result(USER_ALREADY_ACTIVATED, u);
        u.setStatus(UserStatus.ACTIVE);
        dao.update(u);
        return new Result(u);
    }

    @Transactional(readOnly = true)
    public Result authorize(String login, String password) {
        //WARNING login is email
        User u = dao.findByEmail(login);
        if (u == null)
            return new Result(USER_NOT_FOUND);
        if (!password.equals(u.getPsw())) {
            return new Result(USER_INCORRECT_PASSWORD);
        }
        if (!UserStatus.ACTIVE.equals(u.getStatus())) {
            return new Result(USER_NOT_ACTIVATED);
        }
        return new Result(u);
    }

    @Transactional(readOnly = true)
    public boolean isLoginExists(String login) {
        return dao.isLoginExists(login);
    }

    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return dao.isEmailExists(email);
    }

    @Transactional(readOnly = true)
    public Result passwordRestore(String identity, Locale locale) {
        User u = dao.findByLogin(identity);
        if (u == null) {
            u = dao.findByEmail(identity);
        }
        if (u == null)
            return new Result(USER_NOT_FOUND);
        try {
            sendPasswordRestoreEmail(u, locale);
        } catch (MessagingException e) {
            log.error("Error during send password restore message:", e);
            return new Result(e);
        }
        return new Result(u);
    }


}
