clear all
close all
clc

% Parameters:
%
% - Fs       : sampling frequency
% - F0       : frequency of the notes forming chord
% - gain     : gains of individual notes in the chord
% - duration : duration of the chord in second
% - alpha    : attenuation in KS algorithm

Fs = 48000;

% D2, D3, F3, G3, F4, A4, C5, G5
F0 = 440*[2^-(31/12); 2^-(19/12); 2^-(16/12); 2^(-14/12); 2^-(4/12); 1; 2^(3/12); 2^(10/12)];
gain = [1.2 3.0 1.0 2.2 1.0 1.0 1.0 3.5];
duration = 4;
alpha = 0.9785;

% Number of samples in the chord
nbsample_chord = Fs*duration;

% This is used to correct alpha later, so that all the notes decay together
% (with the same decay rate)
first_duration = ceil(nbsample_chord / round(Fs/F0(1)));

% Initialization
chord = zeros(nbsample_chord, 1);

for i = 1:length(F0)
    
    % Get M and duration parameter
    current_M = round(Fs/F0(i));
    current_duration = ceil(nbsample_chord/current_M);

    % Correct current alpha so that all the notes decay together (with the
    % same decay rate)
    current_alpha = alpha^(first_duration/current_duration);

    % Let Paul's high D on the bass ring a bit longer
    if i == 2
        current_alpha = current_alpha^.8;
    end

    % Generate input and output of KS algorithm
    x = rand(current_M, 1);
    y = ks(x, current_alpha, current_duration);
    y = y(1:nbsample_chord);
        
    % Construct the chord by adding the generated note (with the
    % appropriate gain)
    chord = chord + gain(i) * y;
end

% Play output
soundsc(chord, Fs);