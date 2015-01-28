function y = ks(x, alpha, D)
    
% Length of the output signal must be larger than the length of the input signal,
% that is, D must be larger than 1 
if D < 1   
    error('Duration D must be greater than 1.');
end  
    
% Make sure the input is a row-vector 
x = x(:).';

% Number of input samples
M = length(x);

% number of output samples
size_y = D * M;

% Create a vector of the powers of alpha, [alpha^0 alpha^1 ....]
size_alphaVector = D;
alphaVector = (alpha*ones(size_alphaVector,1)).^((0:(size_alphaVector-1))');

% Create a matrix with M columns, each being the vector of the powers of alpha
alphaMatrix = repmat(alphaVector, 1, M);

% Create a matrix with D rows filled by the input signal x   
xMatrix = repmat(x, D, 1);

% Multipliy the two, and take the transpose so we can read it out
% column-by-column
yMatrix = (alphaMatrix .* xMatrix).';

% Read out the output column by columnn
y = yMatrix(:);

return