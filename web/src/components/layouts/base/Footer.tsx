const Footer = () => {
  return (
    <div className="w-full">
      <div className="flex justify-center h-40 border-t border-gray-200">
        <div className="flex flex-col justify-center items-center gap-0.5 font-thin text-sm w-full">
          <div>© 2024 fluffy. All rights reserved.</div>
          <div>
            created by:
            <a
              href="https://github.com/alstn113"
              target="_blank"
              rel="noopener noreferrer"
              className="font-semibold hover:underline ml-1"
            >
              @alstn113
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Footer;
