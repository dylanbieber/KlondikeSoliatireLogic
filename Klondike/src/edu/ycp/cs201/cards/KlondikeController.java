package edu.ycp.cs201.cards;

import java.util.ArrayList;

/**
 * The controller class implements all of the logic required to
 * play a game of Klondike.  All of the data is represented
 * in a {@link KlondikeModel} object, and each controller method
 * takes a reference to the model object as a parameter. 
 */
public class KlondikeController {
	/**
	 *	Initialize the model object.
	 * Should populate and shuffle the main deck, and then
	 * deal cards from the main deck onto the seven tableau piles.
	 * 
	 * It should set the expose index of each tableau pile
	 * so that only the top card is exposed.
	 * 
	 * It should set the expose index of the main deck so that
	 * only the top card is exposed.
	 * 
	 * It should set the expose index of the waste pile so that
	 * no cards can ever be exposed (i.e., set it to a high value,
	 * such that even if it contains all 52 cards, none of them
	 * would be exposed).
	 * 
	 * It should set the expose index of each foundation pile to 0.
	 * 
	 * @param model the {@link KlondikeModel} object to initialize
	 */
	public void initModel(KlondikeModel model) {
		
		//populate deck
		model.getMainDeck().populate();
		
		//shuffle deck
		model.getMainDeck().shuffle();
		
		//deal to tableau piles 0-6
		for(int i=1; i<8; i++) {
			
			//put number of cards to transfer in a List 
			ArrayList<Card> transferPile = model.getMainDeck().removeCards(i);
			
			//add the List to the next tableau index
			model.getTableauPile(i-1).addCards(transferPile);
		}
		
		
		//set expose indexes for each pile in model
		
			//set expose indexes for foundation piles 0-3
			for(int i=0; i<4; i++) {
				model.getFoundationPile(i).setExposeIndex(0);
			}
			
			//set expose indexes for tableau piles 0-6
			for(int i=0; i<7 ;i++) {
				model.getTableauPile(i).setExposeIndex((model.getTableauPile(i).getNumCards())-1);
			}
			
			//set expose index for main pile
			model.getMainDeck().setExposeIndex((model.getMainDeck().getNumCards())-1);
			
			//set expose index for waste pile
			model.getWastePile().setExposeIndex(9001);
		
	}

	/**
	 * <p>Attempt to create a {@link Selection} that represents one
	 * or more {@link Card}s to be moved from a pile
	 * indicated by a {@link Location}.  
	 * 
	 * The {@link Location} specifies
	 * which pile to move cards from, and which card or cards
	 * within the pile should be moved.  Note that this method
	 * must check to see whether the proposed move is legal:
	 * it should return <code>null</code> if it is not legal
	 * to move the card or cards indicated by the {@link Location}.</p>
	 * 
	 * <p>If the {@link Location} has {@link LocationType#MAIN_DECK} as
	 * its location type, then the main deck must not be empty,
	 * the selected card must be the top card on the main deck.
	 * </p>
	 * 
	 * <p>If the {@link Location} has {@link LocationType#TABLEAU_PILE}
	 * as its location type, then the {@link Location}'s card index
	 * must refer to a valid card, and the card index must be
	 * greater than or equal to the tableau pile's expose index.</p>
	 * 
	 * <p>It is not legal to move cards from the waste pile or from
	 * a foundation pile, so if the {@link Location}'s location type
	 * is {@link LocationType#WASTE_PILE} or {@link LocationType#FOUNDATION_PILE},
	 * then the method should return null.</p>
	 * 
	 * <p>If the move is legal, the indicated cards should be removed
	 * from the source pile (as indicated by the {@link Location}),
	 * and transfered into the {@link Selection} object returned from
	 * the method.
	 * 
	 * @param model   the {@link KlondikeModel}
	 * @param location the {@link Location} specifying which card or cards to move
	 * @return a {@link Selection} containing the cards to move and the
	 *         {@link Location}, or null if the {@link Location} does not
	 *         indicate a legal location from which cards can be moved
	 */
	public Selection select(KlondikeModel model, Location location) {
		
		//make transfer pile
		Pile transfer = new Pile();
		
		//location is main, which is populated
		if(location.getLocationType()==LocationType.MAIN_DECK) {
			if(!model.getMainDeck().isEmpty() && location.getCardIndex() == model.getMainDeck().getIndexOfTopCard()) {
				//add top card from model to transfer
				transfer.addCards(model.getMainDeck().removeCards(1));
			}
			else {
				return null;
			}
			
		}
		//location is tableau
		else if(location.getLocationType()==LocationType.TABLEAU_PILE) {
			
			//if card index is less than number of cards in the pile
			if(location.getCardIndex() < model.getTableauPile(location.getPileIndex()).getNumCards() && location.getCardIndex() >= model.getTableauPile(location.getPileIndex()).getExposeIndex()) {
			
				//add cards to be transfered to the pile
				transfer.addCards(model.getTableauPile(location.getPileIndex()).removeCards(model.getTableauPile(location.getPileIndex()).getNumCards()-location.getCardIndex()));
			}
			else {
				return null;
			}

		}
		
		//location is waste or foundation
		else if(location.getLocationType() == LocationType.FOUNDATION_PILE || location.getLocationType() == LocationType.WASTE_PILE){
			return null;
		}
		
		//make the selection
		Selection select = new Selection(location,transfer.removeCards(transfer.getNumCards()));
		
		//return
		return select;
	}

	/**
	 * "Undo" a selection by moving cards from a {@link Selection} object
	 * back to the pile they were taken from, as indicated by the
	 * selection's origin {@link Location}.  For example, if the location
	 * indicates that the selection's cards were taken from a tableau
	 * pile, then they should be moved back to that tableau pile.
	 * 
	 * @param model      the {@link KlondikeModel}
	 * @param selection  the {@link Selection} to undo
	 */
	public void unselect(KlondikeModel model, Selection selection) {
		
		//location is main
		if(selection.getOrigin().getLocationType() == LocationType.MAIN_DECK) {
			
			//add cards back to og location
			model.getMainDeck().addCards(selection.getCards());
			
		}
		
		//location is tableau
		else if(selection.getOrigin().getLocationType() == LocationType.TABLEAU_PILE) {
			
			//add cards back to og location
			model.getTableauPile(selection.getOrigin().getPileIndex()).addCards(selection.getCards());
			
		}
	}

	/**
	 * <p>Determine whether it is legal to move the current {@link Selection} to a
	 * destination pile indicated by a {@link Location}.</p>
	 * 
	 * <p>If the destination {@link Location} has {@link LocationType#FOUNDATION_PILE}
	 * as its {@link LocationType}, then the {@link Selection} must contain a single card.
	 * 
	 * If the foundation pile is empty, then the selected card must be an {@link Rank#ACE}.
	 * 
	 * If the foundation pile is not empty, then the selected card must have the same suit
	 * as the top card on the foundation pile, and the ordinal value of its {@link Rank} must
	 * be one greater than the top card on the foundation pile.</p>
	 * 
	 * 
	 * 
	 * 
	 * <p>If the destination {@link Location} has {@link LocationType#TABLEAU_PILE}
	 * as its {@link LocationType}, then:
	 * <ul>
	 *   <li>If the destination tableau pile is empty, the bottom card of the {@link Selection}
	 *   must be {@link Rank#KING} </li>
	 *   
	 *   <li>If the destination tableau pile is not empty, the bottom card of the {@link Selection}
	 *   must have a {@link Suit} that is a different {@link Color} than the top card of the
	 *   tableau pile, and the bottom card of the {@link Selection} must have an
	 *   {@link Rank} whose ordinal value is one less than the ordinal value of the
	 *   top card of the tableau pile.</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>If the destination {@link Location} has {@link LocationType#MAIN_DECK} or
	 * {@link LocationType#WASTE_PILE} as its {@link LocationType}, then the move is not legal.</p>
	 * 
	 * <p>
	 * Note that this method just checks to see whether or not a move would
	 * be legal: it does not move any cards.
	 * </p>
	 * 
	 * @param model      the {@link KlondikeModel}
	 * @param selection  the {@link Selection}
	 * @param dest       the destination {@link Location}
	 * @return true if the move is legal, false if the move is not legal
	 */
	public boolean allowMove(KlondikeModel model, Selection selection, Location dest) {
		
		//if destination is foundation pile
		if(dest.getLocationType() == LocationType.FOUNDATION_PILE) {
			
			//does selection has a single card
			if(selection.getNumCards() == 1) {
				
				//if the foundation pile is empty
				if(model.getFoundationPile(dest.getPileIndex()).isEmpty()) {
					
					//if the single card is an Ace
					if(selection.getCards().get(0).getRank() == Rank.ACE) {
						
						//allow move
						return true;
					}
				}
				
				//foundation pile is populated
				else {
					//if selection suit == top foundation's suit
					if(selection.getCards().get(0).getSuit() == model.getFoundationPile(dest.getPileIndex()).getCard(model.getFoundationPile(dest.getPileIndex()).getNumCards()-1).getSuit()) {
						
						//if rank of selection is 1 greater than  top foundation's rank
						if(selection.getCards().get(0).getRank().ordinal() == 1 + model.getFoundationPile(dest.getPileIndex()).getCard(model.getFoundationPile(dest.getPileIndex()).getNumCards()-1).getRank().ordinal()) {
							
							//allow move
							return true;
						}
					}
				}
			}
		}
		
		//if destination is tableau
		if(dest.getLocationType() == LocationType.TABLEAU_PILE) {
			
			//is dest populated?
			if(model.getTableauPile(dest.getPileIndex()).isEmpty())
				//if dest tableau is empty, king must be bottom in selection
				if(selection.getCards().get(0).getRank() == Rank.KING) {
					
					//allow move
					return true;
				}
			
			//dest of tableau is populated
			else {
				
				//color of bottom selection is not equal to top card of tableau
				if(selection.getCards().get(0).getSuit().getColor() != model.getTableauPile(dest.getPileIndex()).getCard(model.getTableauPile(dest.getPileIndex()).getIndexOfTopCard()).getSuit().getColor()) {
					
					//if rank of selection is 1 greater than  top tableau's rank
					if(selection.getCards().get(0).getRank().ordinal()  == 1 - model.getTableauPile(dest.getPileIndex()).getCard(model.getTableauPile(dest.getPileIndex()).getIndexOfTopCard()).getRank().ordinal()) {
						
						//allow move
						return true;
					}
				}
			}
		}
		
		//if destination is waste or main
		if(dest.getLocationType() == LocationType.MAIN_DECK || dest.getLocationType() == LocationType.WASTE_PILE) {
			return false;
		}
		
		//if no tests pass, fail
		return false;
	}

	/**
	 * <p>Move the cards in a {@link Selection} onto a pile as indicated
	 * by the destination {@link Location}.</p>
	 * 
	 * <p>The method does <em>not</em> need to check whether or not the move is legal:
	 * it can assume that the {@link #allowMove(KlondikeModel, Selection, Location)}
	 * method has already determined that the move is legal.</p>
	 * 
	 * <p>
	 * <strong>Important</strong>: If the pile that the selected cards
	 * were taken from is non-empty, and if its top card is not exposed,
	 * then its top card should be exposed.
	 * </p>
	 * 
	 * <p>
	 * Note that the expose index of the destination pile should not change.
	 * </p>
	 * 
	 * @param model      the {@link KlondikeModel} 
	 * @param selection  the {@link Selection} containing the selected cards
	 * @param dest       the destination {@link Location}
	 */
	public void moveCards(KlondikeModel model, Selection selection, Location dest) {
		
		//if dest is foundation 
		if(dest.getLocationType() == LocationType.FOUNDATION_PILE) {
			model.getFoundationPile(dest.getPileIndex()).addCards(selection.getCards());
		}
		//dest is tableau
		else if(dest.getLocationType() == LocationType.TABLEAU_PILE) {
			model.getTableauPile(dest.getPileIndex()).addCards(selection.getCards());	
		}
		
		//if origin was tableau
		if(selection.getOrigin().getLocationType() == LocationType.TABLEAU_PILE) {
			
			//if pile index is greater than the index of top card
			if(model.getTableauPile(selection.getOrigin().getPileIndex()).getExposeIndex() > model.getTableauPile(selection.getOrigin().getPileIndex()).getNumCards() - 1) {
				
				//set new pile index to the current top card
				model.getTableauPile(selection.getOrigin().getPileIndex()).setExposeIndex(model.getTableauPile(selection.getOrigin().getPileIndex()).getNumCards() - 1);
			}
		}
	
		//if origin was main
		else if(selection.getOrigin().getLocationType() == LocationType.MAIN_DECK) {
			
			//have to set new index
			model.getMainDeck().setExposeIndex(model.getMainDeck().getNumCards() - 1);
		}
	}

	/**
	 * <p>If the main deck is not empty, then remove the top card from the main deck
	 * and add it to the waste pile. 
	 * 
	 *  If the main deck is empty, then all cards
	 * should be moved from the waste pile back to the main deck.</p>
	 * 
	 * <p>Note: when the waste pile is recycled, it should move cards
	 * back to the main deck such that they are in the same order in which
	 * they originally occurred.  In other words, drawing all of the cards
	 * from the main deck and then moving them back to the main deck
	 * should result in the main deck being in its original order.</p>
	 * 
	 * <p>Before this method returns, it should check whether the
	 * main deck is non-empty, and if it is non-empty, it should ensure
	 * that the top card on the main deck is exposed.</p>
	 * 
	 * @param model the {@link KlondikeModel}
	 */
	public void drawCardOrRecycleWaste(KlondikeModel model) {
		
		//if main is not empty
		if(!model.getMainDeck().isEmpty()) {
			//draw card off main and add to waste
			model.getWastePile().addCard(model.getMainDeck().drawCard());
		}
		//if main is empty
		else if(model.getMainDeck().isEmpty()) {
			
			//get num of cards in waste
			int numCards = model.getWastePile().getNumCards();
			
			//remove all cards in waste and add to main by drawing them from waste
			for(int i=0;i<numCards;i++) {
				model.getMainDeck().addCard(model.getWastePile().drawCard());
			}
		
		}
		
		//check top of main card's expose index
		if(!model.getMainDeck().isEmpty()) {
			
			model.getMainDeck().setExposeIndex(model.getMainDeck().getIndexOfTopCard());
		}
		
	}

	/**
	 * Determine if the player has won the game.
	 * 
	 * @param model the {@link KlondikeModel}
	 * @return true if each foundation pile has 13 cards, false otherwise
	 */
	public boolean isWin(KlondikeModel model) {
		int count=0;
		
		//go through 4 piles, if pile has 13 cards, add 1 to count
		for(int i=0; i<4 ;i++) {
			Pile foundationPile = model.getFoundationPile(i);
			if(foundationPile.getNumCards() == 13) {
				count++;
			}
		}

		//if 4 valid piles, return true
		if (count == 4) {
			return true;
		}
		//less than 4 valid piles, return false
		else {
			return false;
		}
	}
}
