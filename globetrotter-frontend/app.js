// API Base URL - Change this to your Spring Boot server URL
const API_BASE_URL = 'https://globetrotter-challenge-production.up.railway.app/api';

// State management
let state = {
    user: {
        username: '',
        inviteCode: '',
        score: 0
    },
    currentGame: {
        sessionId: null,
        correctId: null,
        answered: false
    }
};

// DOM elements
const welcomeScreen = document.getElementById('welcome-screen');
const gameScreen = document.getElementById('game-screen');
const resultScreen = document.getElementById('result-screen');
const inviteSection = document.getElementById('invite-section');
const invitedBySection = document.getElementById('invited-by-section');

// Welcome screen elements
const usernameInput = document.getElementById('username');
const registerBtn = document.getElementById('register-btn');
const inviteLinkInput = document.getElementById('invite-link');
const copyLinkBtn = document.getElementById('copy-link-btn');
const shareWhatsAppBtn = document.getElementById('share-whatsapp-btn');
const inviterName = document.getElementById('inviter-name');
const inviterScore = document.getElementById('inviter-score');

// Game screen elements
const userScore = document.getElementById('user-score');
const cluesList = document.getElementById('clues-list');
const optionsList = document.getElementById('options-list');

// Result screen elements
const correctAnswer = document.getElementById('correct-answer');
const incorrectAnswer = document.getElementById('incorrect-answer');
const correctDestination = document.getElementById('correct-destination');
const destinationName = document.getElementById('destination-name');
const destinationImage = document.getElementById('destination-image');
const funFactsList = document.getElementById('fun-facts-list');
const nextQuestionBtn = document.getElementById('next-question-btn');

// Check if user is coming from an invite link
function checkForInviteCode() {
    const urlParams = new URLSearchParams(window.location.search);
    const inviteCode = urlParams.get('invite');
    
    if (inviteCode) {
        // Fetch inviter's info
        fetch(`${API_BASE_URL}/users/invite/${inviteCode}`)
            .then(response => {
                if (!response.ok) throw new Error('Invalid invite code');
                return response.json();
            })
            .then(data => {
                inviterName.textContent = data.username;
                inviterScore.textContent = data.score;
                invitedBySection.classList.remove('hidden');
            })
            .catch(error => {
                console.error('Error fetching invite:', error);
            });
    }
}

// Register new user
function registerUser() {
    const username = usernameInput.value.trim();
    
    if (!username) {
        alert('Please enter a username');
        return;
    }
    
    fetch(`${API_BASE_URL}/users/register?username=${encodeURIComponent(username)}`, {
        method: 'POST'
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 400) {
                throw new Error('Username already taken');
            }
            throw new Error('Failed to register');
        }
        return response.json();
    })
    .then(data => {
        // Save user info
        state.user.username = data.username;
        state.user.inviteCode = data.inviteCode;
        state.user.score = data.score;
        
        // Update UI
        userScore.textContent = data.score;
        
        // Generate invite link
        const inviteLink = `${window.location.origin}${window.location.pathname}?invite=${data.inviteCode}`;
        inviteLinkInput.value = inviteLink;
        
        // Configure WhatsApp share
        shareWhatsAppBtn.addEventListener('click', () => {
            const shareText = `I'm playing Globetrotter and challenging you to beat my score! Try it here: ${inviteLink}`;
            const whatsappUrl = `https://wa.me/?text=${encodeURIComponent(shareText)}`;
            window.open(whatsappUrl, '_blank');
        });
        
        // Show invite section
        inviteSection.classList.remove('hidden');
        
        // Start game
        startGame();
    })
    .catch(error => {
        alert(error.message);
        console.error('Registration error:', error);
    });
}

// Copy invite link to clipboard
function copyInviteLink() {
    inviteLinkInput.select();
    document.execCommand('copy');
    
    // Visual feedback
    copyLinkBtn.textContent = 'Copied!';
    setTimeout(() => {
        copyLinkBtn.textContent = 'Copy';
    }, 2000);
}

// Start a new game
function startGame() {
    welcomeScreen.classList.add('hidden');
    gameScreen.classList.remove('hidden');
    resultScreen.classList.add('hidden');
    
    // Reset game state
    state.currentGame.answered = false;
    
    // Fetch new question
    fetchQuestion();
}

// Fetch a new question
function fetchQuestion() {
    fetch(`${API_BASE_URL}/game/question?username=${encodeURIComponent(state.user.username)}`)
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch question');
            return response.json();
        })
        .then(data => {
            // Save game data
            state.currentGame.sessionId = data.sessionId;
            state.currentGame.correctId = data.correctId;
            
            // Display clues
            displayClues(data.clues);
            
            // Display options
            displayOptions(data.options);
        })
        .catch(error => {
            console.error('Error fetching question:', error);
            alert('Failed to load question. Please try again.');
        });
}

// Display clues
function displayClues(clues) {
    cluesList.innerHTML = '';
    
    clues.forEach(clue => {
        const li = document.createElement('li');
        li.textContent = clue;
        cluesList.appendChild(li);
    });
}

// Display answer options
function displayOptions(options) {
    optionsList.innerHTML = '';
    
    options.forEach(option => {
        const optionCard = document.createElement('div');
        optionCard.className = 'option-card';
        optionCard.textContent = `${option.name}, ${option.country}`;
        optionCard.dataset.id = option.id;
        
        optionCard.addEventListener('click', () => {
            if (!state.currentGame.answered) {
                submitAnswer(option.id);
            }
        });
        
        optionsList.appendChild(optionCard);
    });
}

// Submit an answer
function submitAnswer(answerId) {
    state.currentGame.answered = true;
    
    fetch(`${API_BASE_URL}/game/answer?sessionId=${state.currentGame.sessionId}&destinationId=${state.currentGame.correctId}&answerId=${answerId}`, {
        method: 'POST'
    })
    .then(response => {
        if (!response.ok) throw new Error('Failed to submit answer');
        return response.json();
    })
    .then(data => {
        // Update user score
        state.user.score = data.score;
        userScore.textContent = data.score;
        
        // Display result
        displayResult(data);
    })
    .catch(error => {
        console.error('Error submitting answer:', error);
        alert('Failed to submit answer. Please try again.');
        state.currentGame.answered = false;
    });
}

// Display result
function displayResult(data) {
    gameScreen.classList.add('hidden');
    resultScreen.classList.remove('hidden');
    
    const destination = data.destination;
    
    // Show correct/incorrect feedback
    if (data.correct) {
        correctAnswer.classList.remove('hidden');
        incorrectAnswer.classList.add('hidden');
        
        // Trigger confetti
        confetti({
            particleCount: 100,
            spread: 70,
            origin: { y: 0.6 }
        });
    } else {
        correctAnswer.classList.add('hidden');
        incorrectAnswer.classList.remove('hidden');
        correctDestination.textContent = `${destination.name}, ${destination.country}`;
    }
    
    // Display destination details
    destinationName.textContent = `${destination.name}, ${destination.country}`;
    destinationImage.src = destination.imageUrl;
    destinationImage.alt = destination.name;
    
    // Display fun facts
    funFactsList.innerHTML = '';
    destination.funFacts.forEach(fact => {
        const li = document.createElement('li');
        li.textContent = fact;
        funFactsList.appendChild(li);
    });
}

// Event listeners
document.addEventListener('DOMContentLoaded', () => {
    checkForInviteCode();
    
    registerBtn.addEventListener('click', registerUser);
    copyLinkBtn.addEventListener('click', copyInviteLink);
    nextQuestionBtn.addEventListener('click', startGame);
});