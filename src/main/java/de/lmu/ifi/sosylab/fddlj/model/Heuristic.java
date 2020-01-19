package de.lmu.ifi.sosylab.fddlj.model;

/** Defines a heuristic for the minmax-algorithm. */
public interface Heuristic {

    /**
     * Evaluates the state.
     *
     * @param state    which will be evaluated
     * @param aiPlayer from whose perspective it shall be evaluated
     * @return {@code double} value of the given state
     */
    double evaluateSituationWithHeuristic(GameState state, Player aiPlayer, int depth);
}
