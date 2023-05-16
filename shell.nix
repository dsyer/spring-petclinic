with import <nixpkgs> { };
pkgs.mkShell{

  name = "env";
  buildInputs = [
    azure-cli
    tilt
    telepresence2
    okteto
  ];

}