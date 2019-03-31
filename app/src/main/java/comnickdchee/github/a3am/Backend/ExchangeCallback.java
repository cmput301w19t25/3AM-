package comnickdchee.github.a3am.Backend;

import java.io.IOException;

import comnickdchee.github.a3am.Models.Exchange;

/**
 * Interface callback class that retrieves exchange data
 * when something in the database changes. These callbacks are
 * found in the getters methods of the backend class, and
 * are used to perform a sequence of actions when something
 * relevant happens.
 */
public interface ExchangeCallback {
    void onCallback(Exchange exchange);
}
