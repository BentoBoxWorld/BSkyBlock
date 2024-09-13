package dev.viaduct.factories.domain.banks;

import lombok.Getter;

@Getter
public class CreditBank {
    private double total;
    /**
     * Default constructor that creates an empty CreditBank.
     */
    public CreditBank() { total = 0; }
    /**
     * Add an amount of credits to the player's total in their
     * CreditBank.
     * @param amt double representing amount of credits to add.
     */
    public void addCredit(double amt) {
        total += amt;
    }
    /**
     * Subtract an amount of credits from the player's total in their
     * CreditBank.
     * @param amt double representing the amount of credits to remove.
     */
    public void subtractCredit(double amt) {
        total -= amt;
    }
    /**
     * Returns true if player's CreditBank is empty.
     * @return boolean
     */
    public boolean isEmpty() {
        return total == 0.0;
    }
}
