package com.epicodus.pocketpomodoro.presenters;

import android.support.annotation.Nullable;

import com.epicodus.pocketpomodoro.Constants;
import com.epicodus.pocketpomodoro.contracts.CreateDeckInputContract;
import com.epicodus.pocketpomodoro.models.Card;
import com.epicodus.pocketpomodoro.models.Translation;
import com.epicodus.pocketpomodoro.models.TranslationService;

import java.util.ArrayList;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Presenter associated with CreateDeckInputFragment
 */
public class CreateDeckInputPresenter implements CreateDeckInputContract.Presenter {
    private ArrayList<Card> mCards = new ArrayList<>();
    private CreateDeckInputContract.View mView;
    private Subscription mSubscription;
    private Card mTranslatedCard;

    public CreateDeckInputPresenter(CreateDeckInputContract.View view) {
        mView = view;
        if(mCards == null) {
            mCards = new ArrayList<>();
        }
    }

    public void translateQuestion(String language, String question) {
        mView.setTextTranslate();
        final String text = question;

        if(mSubscription != null) {
            mSubscription.unsubscribe();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.YANDEX_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        TranslationService translationService = retrofit.create(TranslationService.class);

        mSubscription = translationService.translate(Constants.YANDEX_API_KEY, language, question)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Translation>() {
                    @Override
                    public void onCompleted() {
                        mCards.add(mTranslatedCard);
                        mView.addCards(mCards);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Translation translation) {
                        CreateDeckInputPresenter.this.mTranslatedCard = new Card(text, translation.getText().get(0));
                    }
                });

    }

    public boolean checkQuestionValidity(String question, @Nullable String answer) {
        if(answer != null) {
            if(answer.length() == 0) {
                return false;
            }
        }
        if(question.length() > 0) {
            if(mCards.size() == 0) {
                return true;
            } else {
                for(int i = 0; i < mCards.size(); i++) {
                    if(mCards.get(i).getQuestion().equals(question)) {
                        mView.makeErrorToast("This question has already been added");
                        return false;
                    }
                }
                return true;
            }
        } else {
            mView.makeErrorToast("Please fill out question form");
            return false;
        }
    }

    public void setCards(ArrayList<Card> cards) {
        mCards = cards;
    }

    public void addCard(String question, String answer) {
        Card card = new Card(question, answer);
        mCards.add(card);
        mView.informActivity(mCards);
    }
}
