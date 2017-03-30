package com.purchase_agent.webapp.giraffe.script;

import com.googlecode.objectify.ObjectifyService;
import com.purchase_agent.webapp.giraffe.objectify_entity.Transaction;
import com.purchase_agent.webapp.giraffe.utils.Currency;
import com.purchase_agent.webapp.giraffe.utils.MoneyAmount;
import org.joda.time.DateTime;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by lukez on 3/18/17.
 */
public class AddTransactions {
    public static void main(String[] strings) {
        ScriptUtil util = new ScriptUtil();
        util.login();

        Transaction transaction = new Transaction();
        transaction.setStatus(Transaction.Status.RESERVE);
        transaction.setCustomerId(1);
        transaction.setCreationTime(DateTime.now());
        transaction.setDeleted(false);
        transaction.setId(UUID.randomUUID().toString());
        transaction.setMoneyAmount(new MoneyAmount());
        transaction.getMoneyAmount().setAmount(BigDecimal.ONE);
        transaction.getMoneyAmount().setCurrency(Currency.RMB);
        transaction.setSaler("test");
        ObjectifyService.ofy().save().entity(transaction).now();
        util.logout();
        System.exit(0);
    }
}
