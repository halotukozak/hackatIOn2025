# Vision

Our application aims to assist students in finding compatible roommates by leveraging a concept similar to popular dating apps like Tinder. During the onboarding process, users complete a detailed questionnaire to specify their preferences and personal traits. This helps ensure better compatibility between potential roommates. The questionnaire includes aspects such as:

## User info
- Sleep schedule (scale: early bird to night owl)
- Hobbies and interests (list of hobbies)
- Smoking habits (scale: non-smoker to frequent smoker)
- Drinking habits (scale: non-drinker to frequent drinker)
- Personality type (scale: introverted to extroverted)
- Year of study (integer)
- Faculty or department (categorical selection)
- Relationship status (categorical selection)

## Preferences info
- Sleep schedule (scale: early bird to night owl)
- Hobbies and interests (scale: not important to very important)
- Smoking habits (scale: non-smoker to frequent smoker)
- Drinking habits (scale: non-drinker to frequent drinker)
- Personality type (scale: introverted to extroverted)
- Year of study (scale: first year to final year)
- Faculty or department (matter/doesnt matter)
- Relationship status (matter/ doesn't matter)

The user interface is designed to resemble that of dating apps, providing a familiar and intuitive experience. Users are presented with profiles that include key information and a compatibility score, prioritizing the best matches first.

The interaction is simple:
- Swipe right if the profile seems like a good match.
- Swipe left to skip the profile.

When two users mutually express interest, their contact information is shared, enabling them to connect and discuss further. Additionally, users have the flexibility to remove themselves from the system if they are no longer interested in finding a roommate.

## Key Features

- User registration and logging system
- Detailed onboarding questionnaire to gather user preferences and traits.
- Compatibility scoring system to prioritize the best matches.
- Swipe-based interaction for profile selection.
- Mutual interest matching to share contact information.
- Option to deactivate or remove profiles from the system.
- Notifications preview
- User info preview

## Technologies

- **Backend**: Kotlin/Java with Ktor framework.
- **Frontend**: React for building the user interface.
- **Database**: H2 for initial development, with plans for scalability.

## Sorting Algorithm

The system uses a cost function to calculate compatibility scores between users. The algorithm evaluates the differences in user preferences and traits, assigning weights to each aspect based on its importance. For example:

- Smaller differences in sleep schedules or personality types result in lower costs.
- Larger differences in smoking or drinking habits result in higher costs.

The profiles are then sorted by their compatibility scores, with the lowest-cost matches displayed first. This ensures that users are presented with the most compatible roommates at the top of their list.