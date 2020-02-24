package edu.ycp.cs201.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class to represent a pile of {@link Card}s.
 * 
 * Each card has an index, with index 0 being the card
 * at the bottom of the pile.
 * 
 * Each pile has an "expose index": all cards whose
 * indices are greater than or equal to the expose index
 * are face-up, and all other cards are face-down.
 */
public class Pile {
	// TODO: add fields
	private ArrayList<Card> pile;
	private int exposeIndex;
	
	/**
	 * Constructor.  The pile will be empty initially,
	 * and its expose index will be set to 0.
	 */
	
	public Pile() {
		pile = new ArrayList<Card>();
		exposeIndex = 0;
	}

	/**
	 * @return the expose index
	 */
	
	public int getExposeIndex() {
		return exposeIndex;
	}
	
	/**
	 * Set the expose index.
	 * 
	 * @param exposeIndex the expose index to set
	 */
	
	public void setExposeIndex(int exposeIndex) {
		this.exposeIndex = exposeIndex;
	}
	
	/**
	 * Add a {@link Card} to the pile.  The card added is placed
	 * on top of the cards currently in the pile.
	 * 
	 * @param card the {@link Card} to add
	 */
	
	public void addCard(Card card) {
		pile.add(card);
	}

	/**
	 * @return the number of @{link Card}s in the pile
	 */
	public int getNumCards() {
		return pile.size();
	}
	
	/**
	 * @return true if the pile is empty, false otherwise
	 */
	public boolean isEmpty() {
		//pile is empty
		if(pile.size() == 0) {
			return true;
		}
		//pile isnt empty
		else {
			return false;
		}
	}
	
	/**
	 * Get the {@link Card} whose index is given.
	 * 
	 * @param index the index of the card to get
	 * @return the {@link Card} at the index
	 * @throws NoSuchElementException if the index does not refer to a valid card
	 */
	public Card getCard(int index) {
		//card index DNE
		if(index<0 || index>51) {
			throw new NoSuchElementException("THAT CARD DONT EXIST");
		}
		//if pile has no cards
		else if(pile.isEmpty()) {
			throw new NoSuchElementException("THAT PILE IS EMPTY");
		}
		//if here, can check pile index
		else {
			return pile.get(index);
		}
	}

	/**
	 * Get the {@link Card} on top of the pile.
	 * 
	 * @return the {@link Card} on top of the pile
	 * @throws NoSuchElementException if the pile is empty
	 */
	public Card getTopCard() {
		//if pile is empty
		if(pile.isEmpty()) {
			throw new NoSuchElementException("THAT PILE IS EMPTY");
		}
		//pile has cards
		else {
			return pile.get(pile.size()-1);
		}
	}
	
	/**
	 * @return the index of the top {@link Card}, or -1 if the pile is empty
	 */
	public int getIndexOfTopCard() {
		//if pile is empty
		if(pile.size() == 0){
			return -1;
		}
		//is pile has members
		else {
			return (pile.size()-1);
		}
	}
	
	/**
	 * Remove given number of {@link Card}s from the top of the pile.
	 * 
	 * @param numCards number of cards to remove
	 * @return an ArrayList containing the removed cards
	 * @throws IllegalArgumentException if the pile does not have enough {@link Card}s to satisfy the request
	 */
	public ArrayList<Card> removeCards(int numCards) {
		//make arrList to store pending removed cards
		ArrayList<Card> removedCards = new ArrayList<Card>();
		
		//if more cards want to be removed than the size of pile
		if(pile.size() < numCards) {
			throw new IllegalArgumentException("NOT ENOUGH CARDS IN PILE");
		}
		//remove the top card of pile and add it to the 0th index of the remove arrList
		for(int i=0; i<numCards; i++) {
			removedCards.add(0,pile.remove(pile.size()-1));
		}
		//return the remove arrList
		return removedCards;
	}
	
	/**
	 * Add {@link Card}s to the top of the pile.
	 * 
	 * @param cardsToAdd an ArrayList containing the {@link Card}s to add
	 */
	public void addCards(ArrayList<Card> cardsToAdd) {
	
		//make a variable to determine how cards we have to add
		int numCards = cardsToAdd.size();
		
		//add the 0th card of the array, numCards times
		for(int i=0;i<numCards;i++) {
			pile.add(cardsToAdd.remove(0));
		}
		
		
	}
	
	/**
	 * Populate the pile by adding 52 {@link Card}s
	 * representing all possible combinations of
	 * {@link Suit} and {@link Rank}.
	 */
	public void populate() {
		//4 suits
    	for (int j=0; j<4; j++) {
    		//13 ranks
    	    for (int i=0; i<13; i++) {
    	        // use allSuits[j] and allRanks[i] to create a Card
    	    	pile.add(new Card(Rank.values()[i],Suit.values()[j]));
    	    }
    	}
	}

	/**
	 * Shuffle the {@link Card}s in the pile by rearranging
	 * them randomly.
	 */
	public void shuffle() {
		Collections.shuffle(pile);
	}
	
	/**
	 * Remove the top {@link Card} on the pile and return it.
	 * 
	 * @return the removed {@link Card}
	 * @throws NoSuchElementException if the pile is empty
	 */
	
	public Card drawCard() {
		//if pile has no members
		if(pile.isEmpty()) {
			throw new NoSuchElementException("NOT ENOUGH CARDS IN PILE");
		}
		
		//remove card and store in variable
		Card drawn = pile.remove(pile.size()-1);

		//return variable
		return drawn;
	}
}
