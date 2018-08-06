export default {
  // using Vue' global process.env object configured in /frontend/config/
  API_SERVER_URL: process.env.API_SERVER_URL,
  get DEFAULT_AVATAR_URL () { return this.API_SERVER_URL + '/avatars' },
  DEFAULT_IMAGE_PATH: '/images/blogen-logo.png',
  DEFAULT_AVATAR: 'avatar0.jpg'
}
