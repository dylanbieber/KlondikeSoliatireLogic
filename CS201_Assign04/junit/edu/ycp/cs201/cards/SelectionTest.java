package edu.ycp.cs201.cards;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class SelectionTest {
	private Location mainDeckTarget;
	private Selection mainDeckSelection;
	
	private Location tableauPileTarget;
	private Selection tableauPileSelection;
	
	@Before
	public void setUp() {
		// Selection from main deck (just top card selected)
		ArrayList<Card> selectedFromMainDeck = new ArrayList<Card>();
		selectedFromMainDeck.add(new Card(Rank.KING, Suit.SPADES));
		mainDeckTarget = new Location(LocationType.MAIN_DECK, 0, 51);
		mainDeckSelection = new Selection(mainDeckTarget, selectedFromMainDeck);
		
		// Selection from tableau pile (with multiple cards being selected)
		tableauPileTarget = new Location(LocationType.TABLEAU_PILE, 4, 2);
		ArrayList<Card> selectedFromTableauPile = new ArrayList<Card>();
		selectedFromTableauPile.add(new Card(Rank.FOUR, Suit.HEARTS));
		selectedFromTableauPile.add(new Card(Rank.THREE, Suit.CLUBS));
		selectedFromTableauPile.add(new Card(Rank.TWO, Suit.DIAMONDS));
		tableauPileSelection = new Selection(tableauPileTarget, selectedFromTableauPile);
	}
	
	@Test
	public void testGetOrigin() throws Exception {
		assertEquals(mainDeckTarget, mainDeckSelection.getOrigin());
		assertEquals(tableauPileTarget, tableauPileSelection.getOrigin());
	}
	
	@Test
	public void testGetCards() throws Exception {
		ArrayList<Card> selectedFromMainDeck = mainDeckSelection.getCards();
		assertEquals(1, selectedFromMainDeck.size());
		assertEquals(new Card(Rank.KING, Suit.SPADES), selectedFromMainDeck.get(0));
		
		ArrayList<Card> selectedFromTableauPile = tableauPileSelection.getCards();
		assertEquals(3, selectedFromTableauPile.size());
		assertEquals(new Card(Rank.FOUR, Suit.HEARTS), selectedFromTableauPile.get(0));
		assertEquals(new Card(Rank.THREE, Suit.CLUBS), selectedFromTableauPile.get(1));
		assertEquals(new Card(Rank.TWO, Suit.DIAMONDS), selectedFromTableauPile.get(2));
		
	}
	
	@Test
	public void testGetNumCards() throws Exception {
		assertEquals(1, mainDeckSelection.getNumCards());
		assertEquals(3, tableauPileSelection.getNumCards());
	}
}
